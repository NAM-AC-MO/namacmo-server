package com.namacmo.user.api.v1.user.domain.event;

import com.namacmo.appcommon.domain.event.DomainEvent;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.EventType;
import com.namacmo.user.api.v1.user.domain.model.User;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class UserDomainEvent implements DomainEvent<User> {
  private final UUID eventId;
  private final String aggregateId;
  private final String aggregateType;
  private final EventType eventType;

  protected UserDomainEvent(
      UUID eventId,
      String aggregateId,
      String aggregateType,
      EventType eventType
  ) {
    this.eventId = eventId;
    this.aggregateId = aggregateId;
    this.aggregateType = aggregateType;
    this.eventType = eventType;
  }
}
