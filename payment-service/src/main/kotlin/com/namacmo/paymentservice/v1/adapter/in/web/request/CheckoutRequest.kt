package com.namacmo.paymentservice.v1.adapter.`in`.web.request

data class CheckoutRequest(
    val cartId: String,
    val productIds: List<String>,
    val buyerId: String,
    val seed: String
)