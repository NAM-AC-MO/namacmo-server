package com.namacmo.paymentservice.v1.adapter.out.web.toss.executor

import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import com.namacmo.paymentservice.v1.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutor {

  fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}