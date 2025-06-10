package com.namacmo.paymentservice.v1.domain

import com.namacmo.appcommon.domain.valueobject.Money

data class CheckoutResult (
    val amount: Money,
    val orderId: String,
    val orderName: String
)