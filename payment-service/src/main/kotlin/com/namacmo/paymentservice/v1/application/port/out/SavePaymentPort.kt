package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Mono

interface SavePaymentPort {

  fun save(paymentEvent: PaymentEvent): Mono<Void>
}