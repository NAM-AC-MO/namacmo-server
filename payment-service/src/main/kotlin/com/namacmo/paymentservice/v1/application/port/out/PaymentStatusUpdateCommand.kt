package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.PaymentExecutionResult
import com.namacmo.paymentservice.v1.domain.PaymentExtraDetails
import com.namacmo.paymentservice.v1.domain.PaymentFailure
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus


data class PaymentStatusUpdateCommand (
  val paymentKey: String,
  val orderId: String,
  val status: PaymentStatus,
  val extraDetails: PaymentExtraDetails? = null,
  val failure: PaymentFailure? = null
) {

  constructor(paymentExecutionResult: PaymentExecutionResult) : this(
    paymentKey = paymentExecutionResult.paymentKey,
    orderId = paymentExecutionResult.orderId,
    status = paymentExecutionResult.paymentStatus(),
    extraDetails = paymentExecutionResult.extraDetails,
    failure = paymentExecutionResult.failure
  )

  init {
    require(status == PaymentStatus.SUCCESS || status == PaymentStatus.FAILURE || status == PaymentStatus.UNKNOWN) {
      "결제 상태 (status: $status) 는 올바르지 않은 결제 상태입니다."
    }

    if (status == PaymentStatus.SUCCESS) {
      requireNotNull(extraDetails) { "PaymentStatus 값이 SUCCESS 라면 PaymentExtraDetails 는 null 이 되면 안됩니다." }
    } else if (status == PaymentStatus.FAILURE) {
      requireNotNull(failure) { "PaymentStatus 값이 FAILURE 라면 PaymentExecutionFailure 는 null 이 되면 안됩니다. "}
    }
  }
}