package com.namacmo.paymentservice.v1.adapter.out.persistent.exception

import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus

class PaymentAlreadyProcessedException(
  val status: PaymentStatus,
  message: String
) : RuntimeException(message) {
}