package com.namacmo.user.api.v1.level.application.port.in;

import com.namacmo.user.api.v1.level.application.dto.CreateUserLevelCommand;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;

public interface RegisteredUserRequestMessageListener {
  MembershipLevel createUserLevel(CreateUserLevelCommand command);
}
