package com.namacmo.paymentservice.v1.domain.valueobject

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.PendingPaymentOrder

class PendingPaymentOrders(
    private val paymentOrders: List<PendingPaymentOrder>
) {
    fun totalAmount(): Money {
        return paymentOrders
            .map { it.amount }
            .reduce{ acc, money -> acc.add(money)}
    }
}