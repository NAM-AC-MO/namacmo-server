package com.namacmo.paymentservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentCompleteUseCase
import com.namacmo.paymentservice.v1.application.port.out.CompletePaymentPort
import com.namacmo.paymentservice.v1.application.port.out.LoadPaymentPort
import com.namacmo.paymentservice.v1.domain.LedgerEventMessage
import com.namacmo.paymentservice.v1.domain.WalletEventMessage
import reactor.core.publisher.Mono

@UseCase
class PaymentCompleteService (
  private val loadPaymentPort: LoadPaymentPort,
  private val completePaymentPort: CompletePaymentPort
) : PaymentCompleteUseCase {

  override fun completePayment(walletEventMessage: WalletEventMessage): Mono<Void> {
    return loadPaymentPort.getPayment(walletEventMessage.orderId())
      .map { it.apply { confirmWalletUpdate() } }
      .map { it.apply { completeIfDone() } }
      .flatMap { completePaymentPort.complete(it) }
  }

  override fun completePayment(ledgerEventMessage: LedgerEventMessage): Mono<Void> {
    return loadPaymentPort.getPayment(ledgerEventMessage.orderId())
      .map { it.apply { confirmLedgerUpdate() } }
      .map { it.apply { completeIfDone() } }
      .flatMap { completePaymentPort.complete(it) }
  }
}