package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Mono


interface CompletePaymentPort {

  fun complete(paymentEvent: PaymentEvent): Mono<Void>
}