package com.namacmo.paymentservice.test

import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentDatabaseHelper {

  fun getPayments(orderId: String): PaymentEvent?

  fun clean(): Mono<Void>
}