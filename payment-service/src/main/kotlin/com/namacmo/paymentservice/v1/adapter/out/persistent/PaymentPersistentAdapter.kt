package com.namacmo.paymentservice.v1.adapter.out.persistent

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.namacmo.paymentservice.v1.adapter.out.persistent.repository.PaymentRepository
import com.namacmo.paymentservice.v1.application.port.out.SavePaymentPort
import com.namacmo.paymentservice.v1.domain.entity.PaymentEvent
import reactor.core.publisher.Mono

@PersistenceAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository
): SavePaymentPort {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }
}