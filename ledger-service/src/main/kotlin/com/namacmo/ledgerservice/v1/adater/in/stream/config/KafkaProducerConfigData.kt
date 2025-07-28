package com.namacmo.ledgerservice.v1.adater.`in`.stream.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "kafka-producer-config")
data class KafkaProducerConfigData @ConstructorBinding constructor(
    val keySerializerClass: String,
    val valueSerializerClass: String,
    val compressionType: String,
    val acks: String,
    val batchSize: Int,
    val batchSizeBoostFactor: Int,
    val lingerMs: Int,
    val requestTimeoutMs: Int,
    val retryCount: Int,
)