package com.payment.walletservice.v1.adapter.out.web

import com.namacmo.appcommon.domain.valueobject.Money
import com.namacmo.appcommon.hexagonal.WebAdapter
import com.payment.walletservice.v1.adapter.out.web.client.PaymentFeignClient
import com.payment.walletservice.v1.adapter.out.web.response.PaymentOrderResponse
import com.payment.walletservice.v1.application.port.out.ExternalPaymentPort
import com.payment.walletservice.v1.application.port.out.dto.PaymentOrderDto

@WebAdapter
class ExternalPaymentAdapter(
    private val paymentFeignClient: PaymentFeignClient
) : ExternalPaymentPort {
    override fun getPaymentOrders(orderId: String): List<PaymentOrderDto> {
        return paymentFeignClient.getPaymentOrders(orderId).map(::convertToDto)
    }

    private fun convertToDto(paymentOrderResponse: PaymentOrderResponse): PaymentOrderDto {
        return PaymentOrderDto(
            id = paymentOrderResponse.id,
            sellerId = paymentOrderResponse.sellerId.toLong(),
            productId = paymentOrderResponse.productId,
            amount = Money(paymentOrderResponse.amount),
            orderId = paymentOrderResponse.orderId
        )
    }
}