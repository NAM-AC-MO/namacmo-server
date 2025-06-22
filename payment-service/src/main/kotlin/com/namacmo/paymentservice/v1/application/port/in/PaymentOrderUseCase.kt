package com.namacmo.paymentservice.v1.application.port.`in`

import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import reactor.core.publisher.Mono

interface PaymentOrderUseCase {
    fun getPaymentOrders(orderId: String): Mono<PaymentOrders>
}