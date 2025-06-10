package com.namacmo.paymentservice.v1.adapter.out.persistent.repository

import com.namacmo.paymentservice.v1.application.port.out.PaymentStatusUpdateCommand
import com.namacmo.paymentservice.v1.domain.PaymentEventMessage
import com.namacmo.paymentservice.v1.domain.PaymentEventMessageType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PaymentOutboxRepository {

  fun insertOutbox(command: PaymentStatusUpdateCommand): Mono<PaymentEventMessage>

  fun markMessageAsSent(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>

  fun markMessageAsFailure(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>

  fun getPendingPaymentOutboxes(): Flux<PaymentEventMessage>
}