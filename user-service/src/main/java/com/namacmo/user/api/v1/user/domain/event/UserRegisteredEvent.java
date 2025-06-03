package com.namacmo.user.api.v1.user.domain.event;

import com.namacmo.user.api.v1.common.outbox.valueobject.EventType;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRegisteredEvent extends UserDomainEvent {
  private final UserProfile userProfile;
  @Builder
  private UserRegisteredEvent(
      String aggregateId,
      String aggregateType,
      EventType eventType,
      UserProfile userProfile
  ) {
    super(aggregateId, aggregateType, eventType);
    this.userProfile = userProfile;
  }
}
