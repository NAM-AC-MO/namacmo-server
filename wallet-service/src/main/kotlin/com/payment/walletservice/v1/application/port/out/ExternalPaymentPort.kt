package com.payment.walletservice.v1.application.port.out

import com.payment.walletservice.v1.application.port.out.dto.PaymentOrderDto

interface ExternalPaymentPort {
    fun getPaymentOrders(orderId: String): List<PaymentOrderDto>
}