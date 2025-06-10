package com.namacmo.paymentservice.v1.domain

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.valueobject.PendingPaymentOrders

data class PendingPaymentEvent (
  val paymentEventId: String,
  val paymentKey: String,
  val orderId: String,
  val pendingPaymentOrders: PendingPaymentOrders
) {
  fun totalAmount(): Money {
    return pendingPaymentOrders.totalAmount()
  }
}