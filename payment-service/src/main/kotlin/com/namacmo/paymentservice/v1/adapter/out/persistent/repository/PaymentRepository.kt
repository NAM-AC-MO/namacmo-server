package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.paymentservice.v1.domain.PendingPaymentEvent
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PaymentRepository {
    fun save(paymentEvent: PaymentEvent): Mono<Void>
    fun getPendingPayments(): Flux<PendingPaymentEvent>
    fun getPayment(orderId: String): Mono<PaymentEvent>
    fun complete(paymentEvent: PaymentEvent): Mono<Void>
}