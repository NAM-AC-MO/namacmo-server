package com.payment.walletservice.v1.domain.model

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.domain.valueobject.Item
import com.payment.walletservice.v1.domain.valueobject.ReferenceType

class PaymentOrder (
  val id: Long,
  val sellerId: Long,
  amount: Money,
  orderId: String
) : Item(
  amount = amount,
  orderId = orderId,
  referenceId = id,
  referenceType = ReferenceType.PAYMENT_ORDER
)