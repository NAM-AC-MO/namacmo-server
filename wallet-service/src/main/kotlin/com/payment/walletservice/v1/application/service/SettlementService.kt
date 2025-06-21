package com.payment.walletservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.payment.walletservice.v1.adapter.out.persistence.repository.SaveWalletPort
import com.payment.walletservice.v1.application.port.`in`.SettlementUseCase
import com.payment.walletservice.v1.application.port.out.DuplicateMessageFilterPort
import com.payment.walletservice.v1.application.port.out.LoadPaymentOrderPort
import com.payment.walletservice.v1.application.port.out.LoadWalletPort
import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.WalletEventMessage
import com.payment.walletservice.v1.domain.WalletEventMessageType
import com.payment.walletservice.v1.domain.model.PaymentOrder
import com.payment.walletservice.v1.domain.model.Wallet

@UseCase
class SettlementService (
  private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  private val loadPaymentOrderPort: LoadPaymentOrderPort,
  private val loadWalletPort: LoadWalletPort,
  private val saveWalletPort: SaveWalletPort
) : SettlementUseCase {

  override fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage {
    if (duplicateMessageFilterPort.isAlreadyProcess(paymentEventMessage)) {
      return createWalletEventMessage(paymentEventMessage)
    }

    val paymentOrders = loadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId())

    val paymentOrdersBySellerId = paymentOrders.groupBy { it.sellerId }

    val updatedWallets = getUpdatedWallets(paymentOrdersBySellerId)

    saveWalletPort.save(updatedWallets)

    return createWalletEventMessage(paymentEventMessage)
  }

  private fun getUpdatedWallets(paymentOrdersBySellerId: Map<Long, List<PaymentOrder>>): List<Wallet> {
    val sellerIds = paymentOrdersBySellerId.keys

    val wallets = loadWalletPort.getWallets(sellerIds)

    return wallets.map { wallet ->
      wallet.calculateBalanceWith(paymentOrdersBySellerId[wallet.userId]!!)
    }
  }

  private fun createWalletEventMessage(paymentEventMessage: PaymentEventMessage) =
    WalletEventMessage(
      type = WalletEventMessageType.SUCCESS,
      payload = mapOf(
        "orderId" to paymentEventMessage.orderId()
      )
    )

}