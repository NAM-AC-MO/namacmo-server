package com.namacmo.user.api.v1.level.adapter.in.web.message;

import com.namacmo.infracommon.kafka.consumer.KafkaConsumer;
import com.namacmo.infracommon.kafka.model.RegisteredUserAvroModel;
import com.namacmo.user.api.v1.level.application.dto.CreateUserLevelCommand;
import com.namacmo.user.api.v1.level.application.port.in.RegisteredUserRequestMessageListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisteredUserRequestKafkaListener implements KafkaConsumer<RegisteredUserAvroModel> {

  private final RegisteredUserRequestMessageListener registeredUserRequestMessageListener;

  @Override
  @KafkaListener(
      id = "${kafka.consumer.group.registered-user}",
      topics = "${kafka.topics.user.registered}"
  )
  public void receive(
      @Payload List<RegisteredUserAvroModel> messages,
      @Header List<String> keys,
      @Header List<Integer> partitions,
      @Header List<Long> offsets
  ) {
    log.info("{} number of orders approval requests received with keys {}, partitions {} and offsets {}" +
            ", sending for restaurant approval",
        messages.size(),
        keys.toString(),
        partitions.toString(),
        offsets.toString());

    messages.forEach(
        record -> registeredUserRequestMessageListener.createUserLevel(
            new CreateUserLevelCommand(
              record.getUserId(),
              record.getChannelId(),
              LocalDateTime.ofInstant(record.getCreatedAt(), ZoneId.systemDefault())
            )
        ));
  }
}
