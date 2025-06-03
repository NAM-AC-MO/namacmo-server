package com.namacmo.user.api.v1.user.domain.event;

import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.EventType;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRegisteredEvent extends UserDomainEvent {
  private final UserProfile userProfile;
  @Builder
  private UserRegisteredEvent(
      UUID eventId,
      String aggregateId,
      String aggregateType,
      EventType eventType,
      UserProfile userProfile
  ) {
    super(eventId, aggregateId, aggregateType, eventType);
    this.userProfile = userProfile;
  }
}
