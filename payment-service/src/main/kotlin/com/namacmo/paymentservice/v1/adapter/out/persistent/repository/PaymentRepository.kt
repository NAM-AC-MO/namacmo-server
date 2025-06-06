package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentRepository {
    fun save(paymentEvent: PaymentEvent): Mono<Void>
}