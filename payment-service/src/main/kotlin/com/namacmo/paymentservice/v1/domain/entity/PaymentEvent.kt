package com.namacmo.paymentservice.v1.domain.entity

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentMethodGroup
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentType
import java.time.LocalDateTime

data class PaymentEvent (
  val id: String,
  val buyerId: String,
  val orderName: String,
  val orderId: String,
  val paymentKey: String? = null,
  val paymentType: PaymentType? = null,
  val paymentMethodGroup: PaymentMethodGroup? = null,
  val approvedAt: LocalDateTime? = null,
  private val paymentOrders: PaymentOrders,
  private var isPaymentDone: Boolean = false
) {

  fun totalAmount(): Money {
    return paymentOrders.totalAmount()
  }

  fun isPaymentDone(): Boolean = isPaymentDone

  fun isSuccess(): Boolean {
    return paymentOrders.isSuccess()
  }

  fun isFailure(): Boolean {
    return paymentOrders.isFailure()
  }

  fun isUnknown(): Boolean {
    return paymentOrders.isUnknown()
  }

  fun confirmWalletUpdate() {
    paymentOrders.confirmWalletUpdate()
  }

  fun confirmLedgerUpdate() {
    paymentOrders.confirmLedgerUpdate()
  }

  fun completeIfDone() {
    if (allPaymentOrdersDone()) {
      isPaymentDone = true
    }
  }

  fun isLedgerUpdateDone(): Boolean {
    return paymentOrders.isLedgerUpdateDone()
  }

  fun isWalletUpdateDone(): Boolean {
    return paymentOrders.isWalletUpdateDone()
  }

  private fun allPaymentOrdersDone(): Boolean {
    return paymentOrders.allPaymentOrdersDone()
  }

  fun getPaymentOrders(): List<PaymentOrder> = paymentOrders.getPaymentOrders()
}