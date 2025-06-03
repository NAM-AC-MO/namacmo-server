package com.namacmo.user.api.v1.common.outbox.adapter.out.persistence;

import com.namacmo.appcommon.hexagonal.PersistenceAdapter;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.entity.OutboxEvent;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.exception.OutboxEventNotFoundException;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.repository.OutboxRepository;
import com.namacmo.user.api.v1.common.outbox.application.port.out.UpdateOutboxEventStatusPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class OutboxEventAdapter implements UpdateOutboxEventStatusPort {

  private final OutboxRepository outboxRepository;

  @Override
  public void markOutboxEventSent(UUID eventId) {
    log.info("markOutboxEventSent eventId={}", eventId);
    final OutboxEvent outboxEvent = outboxRepository.findById(eventId)
        .orElseThrow(OutboxEventNotFoundException::new);

    outboxEvent.markOutboxEventSent();
  }

  @Override
  public void markOutboxEventFailed(UUID eventId) {
    log.info("markOutboxEventFailed eventId={}", eventId);
    final OutboxEvent outboxEvent = outboxRepository.findById(eventId)
        .orElseThrow(OutboxEventNotFoundException::new);

    outboxEvent.markOutboxEventFailed();
  }
}
