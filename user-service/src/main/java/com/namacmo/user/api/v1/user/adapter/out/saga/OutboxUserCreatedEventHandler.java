package com.namacmo.user.api.v1.user.adapter.out.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.entity.OutboxEvent;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.factory.GatheringEventCriteria;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.factory.OutboxEventFactory;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.repository.OutboxRepository;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.OutboxType;
import com.namacmo.user.api.v1.common.outbox.application.port.out.UpdateOutboxEventStatusPort;
import com.namacmo.user.api.v1.user.adapter.out.saga.command.UserRegisteredMessage;
import com.namacmo.user.api.v1.user.domain.event.UserRegisteredEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxUserCreatedEventHandler {

  private final UserSagaCommandPublisher publisher;
  private final ObjectMapper objectMapper;
  private final OutboxRepository outboxRepository;
  private final UpdateOutboxEventStatusPort updateOutboxEventStatusPort;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(UserRegisteredEvent event) {
    log.info("[save outbox] domain event for {}: {}", event.getAggregateId(), event.getAggregateId());

    GatheringEventCriteria criteria = GatheringEventCriteria.builder()
        .eventId(event.getEventId())
        .aggregateType(event.getAggregateType())
        .aggregateId(event.getAggregateId())
        .eventType(event.getEventType())
        .outboxType(OutboxType.PENDING)
        .payload(payloadToJson(event.getUserProfile()))
        .createdAt(LocalDateTime.now())
        .build();

    final OutboxEvent outboxEvent = OutboxEventFactory.create(criteria);
    outboxRepository.save(outboxEvent);
  }

  private String payloadToJson(Object payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization failed", e);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendMessage(UserRegisteredEvent event) {
    log.info("[send message] domain event for {}: {}", event.getAggregateId(), event.getAggregateId());
    final LocalDateTime now = LocalDateTime.now();

    try {
      updateOutboxEventStatusPort.markOutboxEventSent(event.getEventId());
      final UserRegisteredMessage userRegisteredMessage = new UserRegisteredMessage(event.getAggregateId(), now);
      publisher.publish(userRegisteredMessage);
    } catch (Exception e) {
      updateOutboxEventStatusPort.markOutboxEventFailed(event.getEventId());
    }
  }
}
