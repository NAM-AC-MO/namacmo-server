package com.namacmo.user.api.v1.user.domain.event;

import com.namacmo.appcommon.domain.event.DomainEvent;
import com.namacmo.user.api.v1.common.outbox.valueobject.EventType;
import com.namacmo.user.api.v1.user.domain.model.User;
import lombok.Getter;

@Getter
public abstract class UserDomainEvent implements DomainEvent<User> {
  private final String aggregateId;
  private final String aggregateType;
  private final EventType eventType;

  protected UserDomainEvent(
      String aggregateId,
      String aggregateType,
      EventType eventType
  ) {
    this.aggregateId = aggregateId;
    this.aggregateType = aggregateType;
    this.eventType = eventType;
  }
}
