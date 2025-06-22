package com.payment.walletservice.v1.domain.valueobject

import com.payment.walletservice.v1.domain.WalletEventMessage
import com.payment.walletservice.v1.domain.WalletEventMessageType

class PaymentProcessResult(
    val status: Status,
    val orderId: String
) {
    companion object {
        fun success(orderId: String) = PaymentProcessResult(Status.SUCCESS, orderId)
        fun fail(orderId: String) = PaymentProcessResult(Status.FAIL, orderId)
    }

    fun isSuccess(): Boolean = status.isSuccess()

    fun toSuccessPaymentEventMessage(): WalletEventMessage{
        if (status.isFail()) {
            throw IllegalStateException("성공한 경우에만 응답을 받을 수 있습니다.")
        }
        return WalletEventMessage(
            type = WalletEventMessageType.SUCCESS,
            payload = mapOf("orderId" to orderId)
        )
    }
}

enum class Status {
    SUCCESS, FAIL;

    fun isSuccess(): Boolean = this == SUCCESS
    fun isFail(): Boolean = this == FAIL
}