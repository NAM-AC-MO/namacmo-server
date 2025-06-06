package com.namacmo.paymentservice.v1.domain

data class CheckoutResult (
    val amount: String,
    val orderId: String,
    val orderName: String
)