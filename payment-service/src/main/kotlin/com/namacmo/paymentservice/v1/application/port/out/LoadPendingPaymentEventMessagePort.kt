package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.PaymentEventMessage
import reactor.core.publisher.Flux

interface LoadPendingPaymentEventMessagePort {

  fun getPendingPaymentEventMessage(): Flux<PaymentEventMessage>
}