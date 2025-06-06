package com.namacmo.paymentservice.v1.application.port.`in`

import com.namacmo.paymentservice.v1.domain.CheckoutResult
import reactor.core.publisher.Mono

interface CheckoutUseCase {
    fun checkout(command: CheckoutCommand): Mono<CheckoutResult>
}