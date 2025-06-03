package com.namacmo.user.api.v1.level.adapter.out.schedule;

import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.entity.OutboxEvent;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.repository.OutboxRepository;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.OutboxType;
import com.namacmo.user.api.v1.level.application.port.in.CreateUserCommandHandler;
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

  private final OutboxRepository outboxRepository;
  private final CreateUserCommandHandler createUserCommandHandler;

  @Scheduled(fixedDelay = 3000)
  public void processRegisteredUserCommand() {
    final int maxEventsPerBatch = 100;
    final Pageable pageable = Pageable.ofSize(maxEventsPerBatch);

    List<OutboxEvent> outboxEvents = outboxRepository.findAllByOutboxType(OutboxType.PENDING, pageable);

//    createUserCommandHandler.handle();

  }
}
