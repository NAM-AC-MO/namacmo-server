package com.payment.walletservice.v1.adapter.out.persistence.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigData @ConstructorBinding constructor(
    val bootstrapServers: String,
    val schemaRegistryUrlKey: String,
    val schemaRegistryUrl: String,
    val numOfPartitions: Int,
    val replicationFactor: Short,
)