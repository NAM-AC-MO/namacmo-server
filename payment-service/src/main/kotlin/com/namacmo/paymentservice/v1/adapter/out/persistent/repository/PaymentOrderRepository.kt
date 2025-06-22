package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import reactor.core.publisher.Mono

interface PaymentOrderRepository {
    fun findByOrderId(orderId: String): Mono<PaymentOrders>
}