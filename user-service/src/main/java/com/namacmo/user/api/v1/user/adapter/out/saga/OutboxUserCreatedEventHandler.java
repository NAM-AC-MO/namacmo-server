package com.namacmo.user.api.v1.user.adapter.out.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namacmo.user.api.v1.common.outbox.entity.OutboxEvent;
import com.namacmo.user.api.v1.common.outbox.factory.GatheringEventCriteria;
import com.namacmo.user.api.v1.common.outbox.factory.OutboxEventFactory;
import com.namacmo.user.api.v1.common.outbox.repository.OutboxRepository;
import com.namacmo.user.api.v1.common.outbox.valueobject.OutboxType;
import com.namacmo.user.api.v1.user.adapter.out.saga.command.UserRegisteredMessage;
import com.namacmo.user.api.v1.user.domain.event.UserRegisteredEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxUserCreatedEventHandler {

  private final UserSagaCommandPublisher publisher;
  private final ObjectMapper objectMapper;
  private final OutboxRepository outboxRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(UserRegisteredEvent event) {
    log.info("Handling domain event for {}: {}", event.getAggregateId(), event.getAggregateId());

    GatheringEventCriteria criteria = GatheringEventCriteria.builder()
        .aggregateType(event.getAggregateType())
        .aggregateId(event.getAggregateId())
        .eventType(event.getEventType())
        .outboxType(OutboxType.PENDING)
        .payload(payloadToJson(event.getUserProfile()))
        .createdAt(LocalDateTime.now())
        .build();

    final OutboxEvent outboxEvent = OutboxEventFactory.create(criteria);
    outboxRepository.save(outboxEvent);
//    final UserRegisteredMessage userRegisteredMessage = mapToMessage(outboxEvent);
//    publisher.publish(userRegisteredMessage);
  }

  private String payloadToJson(Object payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization failed", e);
    }
  }

  private UserRegisteredMessage mapToMessage(OutboxEvent outboxEvent) {
    return new UserRegisteredMessage(outboxEvent.getAggregateId(), outboxEvent.getCreatedAt());
  }
}
