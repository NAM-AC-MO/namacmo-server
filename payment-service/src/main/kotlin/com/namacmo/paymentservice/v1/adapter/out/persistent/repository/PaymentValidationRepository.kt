package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import reactor.core.publisher.Mono

interface PaymentValidationRepository {
  fun isValid(orderId: String, amount: String): Mono<Boolean>
}