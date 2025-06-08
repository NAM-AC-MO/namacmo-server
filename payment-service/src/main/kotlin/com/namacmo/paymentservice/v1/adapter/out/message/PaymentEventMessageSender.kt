package com.namacmo.paymentservice.v1.adapter.out.message

import com.namacmo.appcommon.hexagonal.MessageAdapter
import com.namacmo.paymentservice.common.Logger
import com.namacmo.paymentservice.v1.adapter.out.persistent.repository.PaymentOutboxRepository
import com.namacmo.paymentservice.v1.application.port.out.DispatchEventMessagePort
import com.namacmo.paymentservice.v1.domain.PaymentEventMessage
import com.namacmo.paymentservice.v1.domain.PaymentEventMessageType
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

@Configuration
@MessageAdapter
class PaymentEventMessageSender (
  private val paymentOutboxRepository: PaymentOutboxRepository,
  private val kafkaSender: KafkaSender<String, PaymentEventMessage>
): DispatchEventMessagePort {

  private val sendResult = Sinks.many().unicast().onBackpressureBuffer<SenderResult<String>>()

  @PostConstruct
  fun handleSendResult() {
    sendResult.asFlux()
      .flatMap {
        Logger.info("send message={}", it.toString())
        when (it.recordMetadata() != null) {
          true -> paymentOutboxRepository.markMessageAsSent(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
          false -> paymentOutboxRepository.markMessageAsFailure(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
        }
      }
      .onErrorContinue { err, _ -> Logger.error("sendEventMessage", err.message ?: "failed to mark the outbox message.", err)  }
      .subscribeOn(Schedulers.newSingle("handle-send-result-event-message"))
      .subscribe()
  }

  fun send(eventMessage: PaymentEventMessage) {
    val partition = eventMessage.metadata["partitionKey"] as Int
    val topic = eventMessage.metadata["topic"] as String
    val orderId = eventMessage.payload["orderId"] as String

    val producerRecord = ProducerRecord(topic, partition, null, orderId, eventMessage)
    val senderRecord = SenderRecord.create(producerRecord, orderId)

    kafkaSender.send(Mono.just(senderRecord))
      .doOnNext { result ->
        sendResult.emitNext(result, Sinks.EmitFailureHandler.FAIL_FAST)
      }
      .doOnError { e ->
        Logger.error("sendEventMessage", e.message ?: "Failed to send Kafka message", e)
      }
      .subscribe()
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  fun dispatchAfterCommit(paymentEventMessage: PaymentEventMessage) {
    dispatch(paymentEventMessage)
  }

  override fun dispatch(paymentEventMessage: PaymentEventMessage) {
    send(paymentEventMessage)
  }

}