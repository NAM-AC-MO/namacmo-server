package com.payment.walletservice.v1.domain.valueobject

import com.namacmo.appcommon.domain.valueobject.Money

open class Item (
  val amount: Money,
  val orderId: String,
  val referenceId: Long, // 연관있는 도메인 식별 아이디
  val referenceType: ReferenceType
)

enum class ReferenceType {
  PAYMENT_ORDER
}