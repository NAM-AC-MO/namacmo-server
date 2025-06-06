package com.namacmo.paymentservice.v1.application.port.out

import reactor.core.publisher.Mono

interface SavePaymentPort {

  fun save(paymentEvent: PaymentEvent): Mono<Void>
}