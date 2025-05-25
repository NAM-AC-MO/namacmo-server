package com.namacmo.user.api.v1.user.adapter.out.saga;

import com.namacmo.user.api.v1.user.adapter.out.saga.command.UserRegisteredMessage;
import com.namacmo.user.api.v1.user.domain.event.UserRegisteredEvent;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreatedEventHandler {

  private final UserSagaCommandPublisher publisher;

  @EventListener
  public void on(UserRegisteredEvent event) {
    log.info("Handling domain event for user: {}", event.userId());
    final UserRegisteredMessage userRegisteredMessage = mapToMessage(event);
    publisher.publish(userRegisteredMessage);
  }

  private UserRegisteredMessage mapToMessage(UserRegisteredEvent domainEvent) {
    return new UserRegisteredMessage(domainEvent.userId(), Instant.now());
  }
}
