package com.payment.walletservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.payment.walletservice.v1.adapter.out.persistence.repository.SaveWalletPort
import com.payment.walletservice.v1.adapter.out.web.client.PaymentFeignClient
import com.payment.walletservice.v1.application.port.`in`.PaymentProcessResultUseCase
import com.payment.walletservice.v1.application.port.`in`.SettlementUseCase
import com.payment.walletservice.v1.application.port.out.DuplicateMessageFilterPort
import com.payment.walletservice.v1.application.port.out.LoadPaymentOrderPort
import com.payment.walletservice.v1.application.port.out.LoadWalletPort
import com.payment.walletservice.v1.application.port.out.dto.PaymentOrderDto
import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.WalletEventMessage
import com.payment.walletservice.v1.domain.model.PaymentOrder
import com.payment.walletservice.v1.domain.model.Wallet
import com.payment.walletservice.v1.domain.valueobject.PaymentProcessResult
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class SettlementService (
  private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  private val loadPaymentOrderPort: LoadPaymentOrderPort,
  private val loadWalletPort: LoadWalletPort,
  private val saveWalletPort: SaveWalletPort,
  private val paymentFeignClient: PaymentFeignClient
) : SettlementUseCase, PaymentProcessResultUseCase {

  private val log = LoggerFactory.getLogger(SettlementService::class.java)

  @Transactional(readOnly = true)
  override fun execute(paymentEventMessage: PaymentEventMessage): PaymentProcessResult {
    val orderId = paymentEventMessage.orderId()
    if (duplicateMessageFilterPort.isAlreadyProcess(paymentEventMessage)) {
      log.info("isAlreadyProcess orderId={}", orderId)
      return PaymentProcessResult.success(orderId)
    }
    return PaymentProcessResult.fail(orderId)
  }

  @Transactional
  override fun processSettlement(
    orderId: String,
    paymentOrdersDto: List<PaymentOrderDto>
  ): WalletEventMessage {
    log.info("payment event message={}", paymentOrdersDto)
//    val paymentOrders = loadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId())
    val paymentOrders = paymentOrdersDto.map { it.toDomain() }
    val paymentOrdersBySellerId = paymentOrders.groupBy { it.sellerId }

    val updatedWallets = getUpdatedWallets(paymentOrdersBySellerId)

    saveWalletPort.save(updatedWallets)
    return PaymentProcessResult.success(orderId).toSuccessPaymentEventMessage()
  }

  private fun getUpdatedWallets(paymentOrdersBySellerId: Map<Long, List<PaymentOrder>>): List<Wallet> {
    val sellerIds = paymentOrdersBySellerId.keys

    val wallets = loadWalletPort.getWallets(sellerIds)

    return wallets.map { wallet ->
      wallet.calculateBalanceWith(paymentOrdersBySellerId[wallet.userId]!!)
    }
  }
}