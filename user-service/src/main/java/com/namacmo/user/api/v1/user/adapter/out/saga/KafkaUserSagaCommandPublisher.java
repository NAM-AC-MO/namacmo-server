package com.namacmo.user.api.v1.user.adapter.out.saga;

import com.namacmo.infracommon.kafka.model.RegisteredUserAvroModel;
import com.namacmo.infracommon.kafka.producer.service.KafkaProducer;
import com.namacmo.user.api.v1.user.adapter.out.saga.command.UserRegisteredMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUserSagaCommandPublisher implements UserSagaCommandPublisher {

  private final KafkaProducer<String, RegisteredUserAvroModel> kafkaProducer;

  @Value("${kafka.topics.user.registered}")
  private String topic;

  @Override
  public void publish(UserRegisteredMessage message) {
    RegisteredUserAvroModel model = new RegisteredUserAvroModel(UUID.randomUUID(), message.userId(), topic);
    kafkaProducer.send(topic, message.userId(), model);
  }
}
