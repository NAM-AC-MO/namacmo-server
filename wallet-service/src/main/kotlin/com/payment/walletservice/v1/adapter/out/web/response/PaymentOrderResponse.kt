package com.payment.walletservice.v1.adapter.out.web.response

data class PaymentOrderResponse (
    val id: String,
    val sellerId: String,
    val productId: String,
    val amount: String,
    val orderId: String,
)