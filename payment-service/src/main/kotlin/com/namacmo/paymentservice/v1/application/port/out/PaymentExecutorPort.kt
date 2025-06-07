package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import com.namacmo.paymentservice.v1.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutorPort {

  fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}