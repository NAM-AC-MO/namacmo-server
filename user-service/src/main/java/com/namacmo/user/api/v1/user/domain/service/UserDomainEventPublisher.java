package com.namacmo.user.api.v1.user.domain.service;

import com.namacmo.appcommon.domain.event.DomainEvent;
import com.namacmo.appcommon.domain.event.publisher.DomainEventPublisher;
import com.namacmo.user.api.v1.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDomainEventPublisher implements DomainEventPublisher<User> {

  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void publish(List<DomainEvent<User>> domainEvents) {
    domainEvents.forEach(eventPublisher::publishEvent);
  }
}
