package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import reactor.core.publisher.Mono

interface PaymentOrderPort {
    fun getPaymentOrders(orderId: String): Mono<PaymentOrders>
}