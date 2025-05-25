package com.namacmo.infracommon.kafka.producer.service.impl;

import com.namacmo.infracommon.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CompletableFuture<SendResult<K, V>> send(String topicName, K key, V message) {
        log.info("Sending message={} to topic={}", message, topicName );
        return kafkaTemplate.send(topicName, key, message);
    }

    @PreDestroy
    public void close(){
        if(kafkaTemplate != null){
            log.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }
}
