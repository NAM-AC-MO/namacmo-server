package com.namacmo.user.api.v1.level.application.port.in;

import com.namacmo.user.api.v1.level.adapter.in.saga.handler.MembershipCommandHandler;

public interface CreateUserCommandHandler extends MembershipCommandHandler {
  void handle(CreateUserCommandEvent commandEvent);
}
