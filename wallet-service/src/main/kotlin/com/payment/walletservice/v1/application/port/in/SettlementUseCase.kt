package com.payment.walletservice.v1.application.port.`in`

import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.WalletEventMessage


interface SettlementUseCase {

  fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage
}