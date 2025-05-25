package com.namacmo.infracommon.kafka.producer.service;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    CompletableFuture<SendResult<K, V>> send(String topicName, K key, V message);
}
