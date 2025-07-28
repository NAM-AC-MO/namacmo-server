package com.namacmo.ledgerservice.v1.domain

data class DoubleAccountsForLedger (
  val to: Account,
  val from: Account
)