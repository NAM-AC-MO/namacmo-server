package com.namacmo.paymentservice.v1.adapter.out.message.config

import com.namacmo.paymentservice.v1.domain.PaymentEventMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonSerializer
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaSenderConfig {

    @Bean
    fun kafkaSender(): KafkaSender<String, PaymentEventMessage> {
        val props = mapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java // Spring Kafka JsonSerializer
        )
        val senderOptions = SenderOptions.create<String, PaymentEventMessage>(props)
        return KafkaSender.create(senderOptions)
    }
}