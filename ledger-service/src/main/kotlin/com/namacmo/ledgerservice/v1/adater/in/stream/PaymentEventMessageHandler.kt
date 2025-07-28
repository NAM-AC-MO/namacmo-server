package com.namacmo.ledgerservice.v1.adater.`in`.stream

import com.namacmo.appcommon.hexagonal.StreamAdapter
import com.namacmo.ledgerservice.v1.application.port.`in`.DoubleLedgerEntryRecordUseCase
import com.namacmo.ledgerservice.v1.domain.LedgerEventMessage
import com.namacmo.ledgerservice.v1.domain.PaymentEventMessage
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.Message
import org.springframework.transaction.annotation.Transactional
import java.util.function.Consumer

@Configuration
@StreamAdapter
class PaymentEventMessageHandler (
  private val doubleLedgerEntryRecordUseCase: DoubleLedgerEntryRecordUseCase,
  private val kafkaTemplate: KafkaTemplate<String, LedgerEventMessage>,
) {

  private val log = LoggerFactory.getLogger(PaymentEventMessageHandler::class.java)

  @KafkaListener(
    topics = ["\${kafka.topics.payment.confirmed}"], // application.yml에 정의된 topic
    groupId = "\${kafka-consumer-config.group-id}",
    containerFactory = "kafkaListenerContainerFactory"
  )
  @Transactional
  fun consume(message: Message<PaymentEventMessage>, acknowledgment: Acknowledgment): Consumer<Message<PaymentEventMessage>> {
    val payload = message.payload
    log.info("PaymentEventMessageHandler consume message: {}", payload)
    return Consumer { message ->
      val ledgerEventMessage = doubleLedgerEntryRecordUseCase.recordDoubleLedgerEntry(message.payload)
      kafkaTemplate.send("ledger", ledgerEventMessage)
      acknowledgment.acknowledge()
    }
  }
}