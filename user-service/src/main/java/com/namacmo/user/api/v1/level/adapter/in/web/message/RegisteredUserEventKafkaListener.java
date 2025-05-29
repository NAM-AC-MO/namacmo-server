package com.namacmo.user.api.v1.level.adapter.in.web.message;

import com.namacmo.infracommon.kafka.consumer.KafkaConsumer;
import com.namacmo.infracommon.kafka.model.RegisteredUserAvroModel;
import com.namacmo.user.api.v1.level.application.dto.CreateUserLevelCommand;
import com.namacmo.user.api.v1.level.application.port.in.RegisteredUserEventHandler;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisteredUserEventKafkaListener implements KafkaConsumer<RegisteredUserAvroModel> {

  private final RegisteredUserEventHandler registeredUserEventHandler;

  @Override
  @KafkaListener(
      id = "${kafka.consumer.group.registered-user}",
      topics = "${kafka.topics.user.registered}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void receive(List<ConsumerRecord<String, RegisteredUserAvroModel>> records) {
    log.info("record size={}", records.size());
    for (ConsumerRecord<String, RegisteredUserAvroModel> record : records) {
      try {
        String key = record.key();
        int partition = record.partition();
        long offset = record.offset();
        RegisteredUserAvroModel message = record.value();

        log.info("Received message: key={}, partition={}, offset={}", key, partition, offset);

        registeredUserEventHandler.handleCreateUserLevel(
            new CreateUserLevelCommand(
                UUID.randomUUID(),
                message.getUserId(),
                message.getChannelId(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()), ZoneId.systemDefault())
            )
        );
      } catch (Exception ignore) {
        // rollback
      }
    }
  }
}
