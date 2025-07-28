package com.namacmo.ledgerservice.v1.domain

open class Item (
  val id: Long,
  val amount: Long,
  val orderId: String,
  val type: ReferenceType
)
