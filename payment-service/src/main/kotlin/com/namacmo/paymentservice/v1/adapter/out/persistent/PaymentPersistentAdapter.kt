package com.namacmo.paymentservice.v1.adapter.out.persistent

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.namacmo.paymentservice.v1.adapter.out.persistent.repository.PaymentRepository
import com.namacmo.paymentservice.v1.adapter.out.persistent.repository.PaymentStatusUpdateRepository
import com.namacmo.paymentservice.v1.adapter.out.persistent.repository.PaymentValidationRepository
import com.namacmo.paymentservice.v1.application.port.out.*
import com.namacmo.paymentservice.v1.domain.PendingPaymentEvent
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@PersistenceAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository,
    private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
    private val paymentValidationRepository: PaymentValidationRepository
): SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort, LoadPendingPaymentPort {

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
}