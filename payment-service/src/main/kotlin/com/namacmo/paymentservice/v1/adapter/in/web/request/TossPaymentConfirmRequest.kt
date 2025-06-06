package com.namacmo.paymentservice.v1.adapter.`in`.web.request

data class TossPaymentConfirmRequest (
    val paymentKey: String,
    val orderId: String,
    val amount: String
)