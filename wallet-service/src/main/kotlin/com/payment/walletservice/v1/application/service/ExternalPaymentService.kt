package com.payment.walletservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.payment.walletservice.v1.application.port.`in`.GetPaymentOrdersUseCase
import com.payment.walletservice.v1.application.port.out.ExternalPaymentPort
import com.payment.walletservice.v1.application.port.out.dto.PaymentOrderDto

@UseCase
class ExternalPaymentService(
    private val externalPaymentPort: ExternalPaymentPort
) : GetPaymentOrdersUseCase {

    override fun getPaymentOrders(orderId: String): List<PaymentOrderDto> {
        return externalPaymentPort.getPaymentOrders(orderId)
    }
}