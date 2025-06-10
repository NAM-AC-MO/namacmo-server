package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.PendingPaymentEvent
import reactor.core.publisher.Flux

interface LoadPendingPaymentPort {
    fun getPendingPayments(): Flux<PendingPaymentEvent>
}