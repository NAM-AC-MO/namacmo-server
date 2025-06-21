package com.payment.walletservice.v1.domain.model

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.domain.valueobject.Item
import com.payment.walletservice.v1.domain.valueobject.TransactionType

data class Wallet (
  val id: Long,
  val userId: Long,
  val version: Int,
  val balance: Money,
  val walletTransactions: List<WalletTransaction> = emptyList()
) {

  fun calculateBalanceWith(items: List<Item>): Wallet {
    return copy(
      balance = balance.add(items.fold(Money.ZERO) { acc, item -> acc.add(item.amount) }),
      walletTransactions = items.map {
        WalletTransaction(
          walletId = this.id,
          amount = it.amount,
          type = TransactionType.CREDIT,
          referenceId = it.referenceId,
          referenceType = it.referenceType,
          orderId = it.orderId
        )
      }
    )
  }
}