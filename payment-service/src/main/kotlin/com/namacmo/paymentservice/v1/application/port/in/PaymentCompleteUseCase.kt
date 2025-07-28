package com.namacmo.paymentservice.v1.application.port.`in`

import com.namacmo.paymentservice.v1.domain.LedgerEventMessage
import com.namacmo.paymentservice.v1.domain.WalletEventMessage
import reactor.core.publisher.Mono

interface PaymentCompleteUseCase {

  fun completePayment(walletEventMessage: WalletEventMessage): Mono<Void>

  fun completePayment(ledgerEventMessage: LedgerEventMessage): Mono<Void>
}