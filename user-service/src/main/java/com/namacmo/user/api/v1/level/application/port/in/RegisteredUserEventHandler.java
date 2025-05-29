package com.namacmo.user.api.v1.level.application.port.in;

import com.namacmo.user.api.v1.level.application.dto.CreateUserLevelCommand;

public interface RegisteredUserEventHandler {
  void handleCreateUserLevel(CreateUserLevelCommand command);
}
