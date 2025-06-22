package com.namacmo.paymentservice.v1.adapter.out.persistent

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.namacmo.paymentservice.v1.adapter.out.persistent.repository.*
import com.namacmo.paymentservice.v1.application.port.out.*
import com.namacmo.paymentservice.v1.domain.PaymentEventMessage
import com.namacmo.paymentservice.v1.domain.PendingPaymentEvent
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import com.namacmo.paymentservice.v1.domain.valueobject.PaymentOrders
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@PersistenceAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository,
    private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
    private val paymentValidationRepository: PaymentValidationRepository,
    private val paymentOutboxRepository: PaymentOutboxRepository,
    private val paymentOrdersRepository: R2DBCPaymentOrderRepository
): SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort,
    LoadPendingPaymentPort, LoadPendingPaymentEventMessagePort, PaymentOrderPort {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }

    override fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId, paymentKey)
    }

    override fun isValid(orderId: String, amount: String): Mono<Boolean> {
        return paymentValidationRepository.isValid(orderId, amount)
    }

    override fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatus(command)
    }

    override fun getPendingPayments(): Flux<PendingPaymentEvent> {
        return paymentRepository.getPendingPayments()
    }

    override fun getPendingPaymentEventMessage(): Flux<PaymentEventMessage> {
        return paymentOutboxRepository.getPendingPaymentOutboxes()
    }

    override fun getPaymentOrders(orderId: String): Mono<PaymentOrders> {
        return paymentOrdersRepository.findByOrderId(orderId)
    }
}