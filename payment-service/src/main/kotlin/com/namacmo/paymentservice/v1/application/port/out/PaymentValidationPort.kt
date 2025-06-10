package com.namacmo.paymentservice.v1.application.port.out

import reactor.core.publisher.Mono

interface PaymentValidationPort {
  fun isValid(orderId: String, amount: String): Mono<Boolean>
}