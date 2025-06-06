package com.namacmo.paymentservice.v1.application.port.`in`

data class CheckoutCommand(
    val cartId: String,
    val buyerId: String,
    val productIds: List<String>,
    val idempotencyKey: String
)