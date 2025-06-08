package com.namacmo.paymentservice.v1.adapter.out.message

import com.namacmo.paymentservice.v1.domain.PaymentEventMessage
import com.namacmo.paymentservice.v1.domain.PaymentEventMessageType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
@Tag("ExternalIntegration")
class PaymentEventMessageSenderTest(
    @Autowired private val paymentEventMessageSender: PaymentEventMessageSender,
    @Value("\${kafka.topics.payment.confirmed}") private val topic: String
) {
    @Test
    fun `should send eventMessage by using partitionKey`() {
        val paymentEventMessages = listOf(
            PaymentEventMessage(
                type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                payload = mapOf(
                    "orderId" to UUID.randomUUID().toString()
                ),
                metadata = mapOf(
                    "partitionKey" to 0,
                    "topic" to topic
                )
            ),
            PaymentEventMessage(
                type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                payload = mapOf(
                    "orderId" to UUID.randomUUID().toString()
                ),
                metadata = mapOf(
                    "partitionKey" to 1,
                    "topic" to topic
                )
            ),
            PaymentEventMessage(
                type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                payload = mapOf(
                    "orderId" to UUID.randomUUID().toString()
                ),
                metadata = mapOf(
                    "partitionKey" to 2,
                    "topic" to topic
                )
            ),
        )

        paymentEventMessages.forEach {
            paymentEventMessageSender.dispatch(it)
        }

        Thread.sleep(10000)
    }
}