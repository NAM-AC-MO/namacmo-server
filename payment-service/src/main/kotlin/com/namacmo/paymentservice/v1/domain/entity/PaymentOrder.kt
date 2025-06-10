package com.namacmo.paymentservice.v1.domain.entity

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentStatus

data class PaymentOrder (
  val id: String,
  val paymentEventId: String,
  val sellerId: String,
  val productId: String,
  val orderId: String,
  val amount: Money,
  val paymentStatus: PaymentStatus,
  private var isLedgerUpdated: Boolean = false,
  private var isWalletUpdated: Boolean = false
) {

  fun isLedgerUpdated(): Boolean = isLedgerUpdated

  fun isWalletUpdated(): Boolean = isWalletUpdated

  fun confirmWalletUpdate() {
    isWalletUpdated = true
  }

  fun confirmLedgerUpdate() {
    isLedgerUpdated = true
  }
}
