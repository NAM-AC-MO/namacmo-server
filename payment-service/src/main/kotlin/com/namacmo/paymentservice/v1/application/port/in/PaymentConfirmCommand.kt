package com.namacmo.paymentservice.v1.application.port.`in`

data class PaymentConfirmCommand (
  val paymentKey: String,
  val orderId: String,
  val amount: Long
)