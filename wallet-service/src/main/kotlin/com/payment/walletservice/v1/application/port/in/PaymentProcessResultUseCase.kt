package com.payment.walletservice.v1.application.port.`in`

import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.valueobject.PaymentProcessResult

interface PaymentProcessResultUseCase {
    fun execute(paymentEventMessage: PaymentEventMessage): PaymentProcessResult
}
