package com.payment.walletservice.v1.adapter.`in`.config

import com.payment.walletservice.v1.adapter.out.persistence.config.KafkaConfigData
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@EnableConfigurationProperties(KafkaConsumerConfigData::class)
class KafkaConsumerConfig<K, V>(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaConsumerConfigData: KafkaConsumerConfigData
) {
    private val log = LoggerFactory.getLogger(KafkaConsumerConfig::class.java)

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        log.info("KafkaConsumerConfig -> kafkaConfigData=$kafkaConfigData kafkaConsumerConfigData=$kafkaConsumerConfigData")
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to kafkaConsumerConfigData.keyDeserializer,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to kafkaConsumerConfigData.valueDeserializer,

            // 실제 사용될 역직렬화 클래스 지정
            ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS to StringDeserializer::class.java,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java,

            // JsonDeserializer 관련 설정
            JsonDeserializer.TRUSTED_PACKAGES to "*",
            JsonDeserializer.VALUE_DEFAULT_TYPE to "com.payment.walletservice.v1.domain.PaymentEventMessage",
            JsonDeserializer.USE_TYPE_INFO_HEADERS to false,

            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to kafkaConsumerConfigData.enableAutoCommit,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaConsumerConfigData.autoOffsetReset,
            kafkaConfigData.schemaRegistryUrlKey to kafkaConfigData.schemaRegistryUrl,
            kafkaConsumerConfigData.specificAvroReaderKey to kafkaConsumerConfigData.specificAvroReader,
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to kafkaConsumerConfigData.sessionTimeoutMs,
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to kafkaConsumerConfigData.heartbeatIntervalMs,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to kafkaConsumerConfigData.maxPollIntervalMs,
            ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG to
                    kafkaConsumerConfigData.maxPartitionFetchBytesDefault *
                    kafkaConsumerConfigData.maxPartitionFetchBytesBoostFactor,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to kafkaConsumerConfigData.maxPollRecords
        )
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<K, V> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<K, V> {
        return ConcurrentKafkaListenerContainerFactory<K, V>().apply {
            consumerFactory = consumerFactory()
            isBatchListener = kafkaConsumerConfigData.batchListener
            setConcurrency(kafkaConsumerConfigData.concurrencyLevel)
            setAutoStartup(kafkaConsumerConfigData.autoStartup)
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
            containerProperties.pollTimeout = kafkaConsumerConfigData.pollTimeoutMs
        }
    }
}