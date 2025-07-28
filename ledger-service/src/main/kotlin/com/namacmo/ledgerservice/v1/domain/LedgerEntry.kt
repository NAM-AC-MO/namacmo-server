package com.namacmo.ledgerservice.v1.domain

data class LedgerEntry (
  val account: Account,
  val amount: Long,
  val type: LedgerEntryType
)

enum class LedgerEntryType {
  CREDIT, DEBIT
}
