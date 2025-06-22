package com.payment.walletservice.v1.application.port.`in`

import com.payment.walletservice.v1.application.port.out.dto.PaymentOrderDto
import com.payment.walletservice.v1.domain.WalletEventMessage


interface SettlementUseCase {
  fun processSettlement(orderId: String, paymentOrders: List<PaymentOrderDto>): WalletEventMessage
}