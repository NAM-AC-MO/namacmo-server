package com.namacmo.paymentservice.v1.adapter.`in`.web.response

import com.namacmo.paymentservice.v1.domain.entity.PaymentOrder

data class PaymentOrderResponse (
    val id: String,
    val sellerId: String,
    val productId: String,
    val amount: String,
    val orderId: String,
) {
    companion object {
        fun from(paymentOrder: PaymentOrder): PaymentOrderResponse {
            return PaymentOrderResponse(
                id = paymentOrder.id,
                sellerId = paymentOrder.sellerId,
                productId = paymentOrder.productId,
                amount = paymentOrder.amount.toStringValue(),
                orderId = paymentOrder.orderId
            )
        }
    }
}