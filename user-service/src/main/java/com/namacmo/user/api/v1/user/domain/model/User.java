package com.namacmo.user.api.v1.user.domain.model;

import com.namacmo.appcommon.domain.entity.AggregateRoot;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.EventType;
import com.namacmo.user.api.v1.user.domain.event.UserRegisteredEvent;
import com.namacmo.user.api.v1.user.domain.valueobject.Role;
import com.namacmo.user.api.v1.user.domain.valueobject.Roles;
import com.namacmo.user.api.v1.user.domain.valueobject.UserId;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public class User extends AggregateRoot<User, UserId> {

  @Getter
  private final UserProfile userProfile;
  private final Roles roles;

  public Set<Role> getRoles() {
    return roles.getRoles();
  }

  public static User create(
      UserProfile profile,
      Roles roles
  ) {
    final User user = User.builder()
        .userId(new UserId())
        .userProfile(profile)
        .roles(roles)
        .build();

    final UserRegisteredEvent domainEvent = UserRegisteredEvent.builder()
        .eventId(UUID.randomUUID())
        .aggregateId(user.getId().getValue())
        .aggregateType(user.getClass().getTypeName())
        .eventType(EventType.USER_JOINED)
        .userProfile(user.getUserProfile())
        .build();

    user.registerEvent(domainEvent);
    return user;
  }

  @Builder
  private User(
      UserId userId,
      UserProfile userProfile,
      Roles roles
  ) {
    super(userId);
    this.userProfile = userProfile;
    this.roles = roles;
  }
}
