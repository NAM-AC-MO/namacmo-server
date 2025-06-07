package com.namacmo.paymentservice.v1.domain

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus

data class PendingPaymentOrder (
  val paymentOrderId: String,
  val status: PaymentStatus,
  val amount: Money,
  val failedCount: Byte,
  val threshold: Byte
)
