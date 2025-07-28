package com.namacmo.ledgerservice.v1.domain

data class LedgerTransaction (
  val referenceType: ReferenceType,
  val referenceId: Long,
  val orderId: String
)
