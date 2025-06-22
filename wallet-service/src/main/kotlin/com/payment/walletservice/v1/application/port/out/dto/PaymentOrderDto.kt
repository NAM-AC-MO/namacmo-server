package com.payment.walletservice.v1.application.port.out.dto

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.domain.model.PaymentOrder

data class PaymentOrderDto(
    val id: String,
    val sellerId: Long,
    val productId: String,
    val amount: Money,
    val orderId: String,
) {
    fun toDomain(): PaymentOrder {
        return PaymentOrder(
            id = this.id,
            sellerId = this.sellerId,
            amount = this.amount,
            orderId = this.orderId
        )
    }
}