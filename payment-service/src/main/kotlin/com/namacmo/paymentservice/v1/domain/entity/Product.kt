package com.namacmo.paymentservice.v1.domain.entity

import com.namacmo.paymentservice.v1.domain.valueobject.Money

data class Product (
  val id: String,
  val amount: Money,
  val quantity: Int,
  val name: String,
  val sellerId: String
)