package com.namacmo.infracommon.kafka.consumer;

import java.util.List;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<ConsumerRecord<String, T>> records);
}
