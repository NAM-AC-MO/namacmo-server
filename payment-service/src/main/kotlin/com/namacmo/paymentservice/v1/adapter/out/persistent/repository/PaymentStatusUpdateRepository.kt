package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.paymentservice.v1.application.port.out.PaymentStatusUpdateCommand
import reactor.core.publisher.Mono

interface PaymentStatusUpdateRepository {
    fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>
    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}