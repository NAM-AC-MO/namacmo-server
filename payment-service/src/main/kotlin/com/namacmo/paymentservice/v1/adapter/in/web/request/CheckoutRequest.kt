package com.namacmo.paymentservice.v1.adapter.`in`.web.request

import java.time.LocalDateTime

data class CheckoutRequest(
    val cartId: String = "1",
    val productIds: List<String> = listOf("1", "2", "3"),
    val buyerId: String = "1",
    val seed: String = LocalDateTime.now().toString()
)