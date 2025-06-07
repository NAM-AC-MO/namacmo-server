package com.namacmo.paymentservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmCommand
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentConfirmUseCase
import com.namacmo.paymentservice.v1.application.port.out.PaymentExecutorPort
import com.namacmo.paymentservice.v1.application.port.out.PaymentStatusUpdateCommand
import com.namacmo.paymentservice.v1.application.port.out.PaymentStatusUpdatePort
import com.namacmo.paymentservice.v1.application.port.out.PaymentValidationPort
import com.namacmo.paymentservice.v1.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

@UseCase
class PaymentConfirmService (
  private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
  private val paymentValidationPort: PaymentValidationPort,
  private val paymentExecutorPort: PaymentExecutorPort,
) : PaymentConfirmUseCase {

  override fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
    return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId, command.paymentKey)
      .filterWhen { paymentValidationPort.isValid(command.orderId, command.amount) }
      .flatMap { paymentExecutorPort.execute(command) }
      .flatMap {
        paymentStatusUpdatePort.updatePaymentStatus(
          command = PaymentStatusUpdateCommand(
            paymentKey = it.paymentKey,
            orderId = it.orderId,
            status = it.paymentStatus(),
            extraDetails = it.extraDetails,
            failure = it.failure
          )
        ).thenReturn(it)
      }
      .map { PaymentConfirmationResult(status = it.paymentStatus(), failure = it.failure) }
  }
}