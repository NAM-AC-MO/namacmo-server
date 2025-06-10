package com.namacmo.paymentservice.v1.adapter.out.web.toss

import com.namacmo.appcommon.hexagonal.WebAdapter
import com.namacmo.paymentservice.v1.adapter.out.web.toss.executor.PaymentExecutor
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import com.namacmo.paymentservice.v1.application.port.out.PaymentExecutorPort
import com.namacmo.paymentservice.v1.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

@WebAdapter
class PaymentExecutorWebAdapter (
  private val paymentExecutor: PaymentExecutor
) : PaymentExecutorPort {

  override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
    return paymentExecutor.execute(command)
  }
}