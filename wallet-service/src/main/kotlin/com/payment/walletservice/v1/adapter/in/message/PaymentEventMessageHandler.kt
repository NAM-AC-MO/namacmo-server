package com.payment.walletservice.v1.adapter.`in`.message

import com.namacmo.appcommon.hexagonal.MessageAdapter
import com.payment.walletservice.v1.application.port.`in`.GetPaymentOrdersUseCase
import com.payment.walletservice.v1.application.port.`in`.PaymentProcessResultUseCase
import com.payment.walletservice.v1.application.port.`in`.SettlementUseCase
import com.payment.walletservice.v1.domain.PaymentEventMessage
import com.payment.walletservice.v1.domain.WalletEventMessage
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.Message
import org.springframework.transaction.annotation.Transactional

@Configuration
@MessageAdapter
class PaymentEventMessageHandler(
    private val settlementUseCase: SettlementUseCase,
    private val processResultUseCase: PaymentProcessResultUseCase,
    private val kafkaTemplate: KafkaTemplate<String, WalletEventMessage>,
    private val getPaymentOrdersUseCase: GetPaymentOrdersUseCase
) {

    private val log = LoggerFactory.getLogger(PaymentEventMessageHandler::class.java)

    @KafkaListener(
        topics = ["\${kafka.topics.payment.confirmed}"], // application.yml에 정의된 topic
        groupId = "\${kafka-consumer-config.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    fun consume(message: Message<PaymentEventMessage>, acknowledgment: Acknowledgment) {
        val payload = message.payload
        log.info("PaymentEventMessageHandler consume message: {}", payload)
        val paymentProcessResult = processResultUseCase.execute(payload)
        if (paymentProcessResult.isSuccess()) {
            kafkaTemplate.send("wallet", paymentProcessResult.toSuccessPaymentEventMessage())
            acknowledgment.acknowledge()
            return
        }
        val paymentOrders = getPaymentOrdersUseCase.getPaymentOrders(payload.orderId())
        val walletEventMessage = settlementUseCase.processSettlement(payload.orderId(), paymentOrders)
        kafkaTemplate.send("wallet", walletEventMessage)
        acknowledgment.acknowledge()
    }
}