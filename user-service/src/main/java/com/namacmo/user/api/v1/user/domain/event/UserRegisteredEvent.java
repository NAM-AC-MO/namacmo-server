package com.namacmo.user.api.v1.user.domain.event;

import com.namacmo.appcommon.domain.event.DomainEvent;
import com.namacmo.user.api.v1.user.domain.model.User;

public record UserRegisteredEvent(String userId) implements DomainEvent<User> {
}
