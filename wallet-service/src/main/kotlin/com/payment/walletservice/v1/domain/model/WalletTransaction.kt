package com.payment.walletservice.v1.domain.model

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.domain.valueobject.ReferenceType
import com.payment.walletservice.v1.domain.valueobject.TransactionType

data class WalletTransaction (
  val walletId: Long,
  val amount: Money,
  val type: TransactionType,
  val referenceId: String,
  val referenceType: ReferenceType,
  val orderId: String
)
