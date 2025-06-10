package com.namacmo.paymentservice.v1.domain

data class PaymentFailure (
  val errorCode: String,
  val message: String
)