package com.payment.walletservice.v1.application.port.`in`

import com.payment.walletservice.v1.application.port.out.dto.PaymentOrderDto

interface GetPaymentOrdersUseCase {
    fun getPaymentOrders(orderId: String): List<PaymentOrderDto>
}