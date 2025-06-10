package com.namacmo.paymentservice.v1.application.port.`in`

import com.namacmo.paymentservice.v1.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

interface PaymentConfirmUseCase {
    fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult>
}