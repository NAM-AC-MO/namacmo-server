package com.namacmo.ledgerservice.v1.adater.`in`.stream.config

import com.namacmo.ledgerservice.v1.domain.LedgerEventMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.*

@Configuration
@EnableConfigurationProperties(KafkaProducerConfigData::class, KafkaConfigData::class)
class KafkaTransactionalProducerConfig(
    private val kafkaProducerConfigData: KafkaProducerConfigData,
    private val kafkaConfigData: KafkaConfigData
) {

    /**
     * 트랜잭션을 지원하는 Producer 설정
     */
    @Bean
    fun producerTransactionalConfig(): Map<String, Any> {
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to kafkaProducerConfigData.keySerializerClass,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to kafkaProducerConfigData.valueSerializerClass,
            ProducerConfig.BATCH_SIZE_CONFIG to kafkaProducerConfigData.batchSize *
                    kafkaProducerConfigData.batchSizeBoostFactor,
            ProducerConfig.LINGER_MS_CONFIG to kafkaProducerConfigData.lingerMs,
            ProducerConfig.COMPRESSION_TYPE_CONFIG to kafkaProducerConfigData.compressionType,
            ProducerConfig.ACKS_CONFIG to kafkaProducerConfigData.acks,

            ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG to kafkaProducerConfigData.requestTimeoutMs,
            ProducerConfig.RETRIES_CONFIG to kafkaProducerConfigData.retryCount,
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,
            ProducerConfig.CLIENT_ID_CONFIG to "ledger-producer",
            ProducerConfig.TRANSACTIONAL_ID_CONFIG to "ledger.tx.id", // Base ID for suffix strategy
            ProducerConfig.TRANSACTION_TIMEOUT_CONFIG to 30000
        )
    }

    /**
     * 트랜잭션을 지원하는 Kafka Producer Factory
     * maxCache = 5 이므로 트랜잭션 ID는 "wallet.tx.id.0" ~ "wallet.tx.id.4" 로 재사용
     */
    @Bean
    fun transactionalProducerFactory(): ProducerFactory<String, LedgerEventMessage> {
        val producerFactory = DefaultKafkaProducerFactory<String, LedgerEventMessage>(producerTransactionalConfig())

        // 🔥 여기서 transaction id suffix 전략을 설정함 (기존에는 suffix 없이 단일 tx.id만 사용됨)
        val suffixStrategy: TransactionIdSuffixStrategy = DefaultTransactionIdSuffixStrategy(5)
        producerFactory.setTransactionIdSuffixStrategy(suffixStrategy)

        return producerFactory
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, LedgerEventMessage> {
        return KafkaTemplate(transactionalProducerFactory())
    }
}
