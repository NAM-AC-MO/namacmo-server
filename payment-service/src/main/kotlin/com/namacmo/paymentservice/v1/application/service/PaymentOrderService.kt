package com.namacmo.paymentservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentOrderUseCase
import com.namacmo.paymentservice.v1.application.port.out.PaymentOrderPort
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import reactor.core.publisher.Mono

@UseCase
class PaymentOrderService(
    private val paymentOrderPort: PaymentOrderPort
) : PaymentOrderUseCase {
    override fun getPaymentOrders(orderId: String): Mono<PaymentOrders> {
        return paymentOrderPort.getPaymentOrders(orderId)
    }
}