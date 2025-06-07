package com.namacmo.paymentservice.v1.domain.valueobject

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.paymentservice.v1.domain.entity.PaymentOrder

class PaymentOrders(
    private val paymentOrders: List<PaymentOrder>
) {
    fun totalAmount(): Money {
        return paymentOrders
            .map { it.amount }
            .reduce{ acc, money -> acc.add(money)}
    }
    fun isSuccess(): Boolean {
        return paymentOrders.all { it.paymentStatus == PaymentStatus.SUCCESS }
    }

    fun isFailure(): Boolean {
        return paymentOrders.all { it.paymentStatus == PaymentStatus.FAILURE }
    }

    fun isUnknown(): Boolean {
        return paymentOrders.all { it.paymentStatus == PaymentStatus.UNKNOWN }
    }

    fun confirmWalletUpdate() {
        paymentOrders.forEach { it.confirmWalletUpdate() }
    }

    fun confirmLedgerUpdate() {
        paymentOrders.forEach { it.confirmLedgerUpdate() }
    }

    fun isLedgerUpdateDone(): Boolean {
        return paymentOrders.all { it.isLedgerUpdated() }
    }

    fun isWalletUpdateDone(): Boolean {
        return paymentOrders.all { it.isWalletUpdated() }
    }

    fun allPaymentOrdersDone(): Boolean {
        return paymentOrders.all { it.isWalletUpdated() && it.isLedgerUpdated() }
    }

    fun getPaymentOrders(): List<PaymentOrder> = paymentOrders
}