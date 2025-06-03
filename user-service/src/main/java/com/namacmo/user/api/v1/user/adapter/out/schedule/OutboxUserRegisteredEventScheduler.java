package com.namacmo.user.api.v1.user.adapter.out.schedule;

import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.entity.OutboxEvent;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.repository.OutboxRepository;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.OutboxType;
import com.namacmo.user.api.v1.common.outbox.application.port.out.UpdateOutboxEventStatusPort;
import com.namacmo.user.api.v1.user.adapter.out.saga.UserSagaCommandPublisher;
import com.namacmo.user.api.v1.user.adapter.out.saga.command.UserRegisteredMessage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxUserRegisteredEventScheduler {

  private final UserSagaCommandPublisher publisher;
  private final UpdateOutboxEventStatusPort updateOutboxEventStatusPort;
  private final OutboxRepository outboxRepository;

  @Scheduled(fixedDelay = 3000)
  public void processRegisteredUserCommand() {
    final int maxEventsPerBatch = 100;
    final Pageable pageable = Pageable.ofSize(maxEventsPerBatch);

    List<OutboxEvent> outboxEvents = outboxRepository.findAllByOutboxType(OutboxType.PENDING, pageable);
    log.info("read events count={}", outboxEvents.size());

    final LocalDateTime now = LocalDateTime.now();
    for (OutboxEvent outboxEvent : outboxEvents) {
      try {
        log.info("[send message] domain event for {}: {}", outboxEvent.getAggregateType(), outboxEvent.getAggregateId());
        updateOutboxEventStatusPort.markOutboxEventSent(outboxEvent.getEventId());
        final UserRegisteredMessage userRegisteredMessage = new UserRegisteredMessage(
            outboxEvent.getEventId(),
            outboxEvent.getAggregateId(),
            now
        );
        publisher.publish(userRegisteredMessage);
      } catch (Exception e) {
        updateOutboxEventStatusPort.markOutboxEventFailed(outboxEvent.getEventId());
      }
    }
  }
}
