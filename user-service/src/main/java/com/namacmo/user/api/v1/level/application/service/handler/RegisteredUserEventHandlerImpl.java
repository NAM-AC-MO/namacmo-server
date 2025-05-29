package com.namacmo.user.api.v1.level.application.service.handler;

import com.namacmo.user.api.v1.level.application.dto.CreateUserLevelCommand;
import com.namacmo.user.api.v1.level.application.port.in.RegisteredUserEventHandler;
import com.namacmo.user.api.v1.level.application.port.out.ExistsUserLevelPort;
import com.namacmo.user.api.v1.level.application.port.out.RegisterUserLevelPort;
import com.namacmo.user.api.v1.level.application.service.saga.RegisteredUserSaga;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import com.namacmo.user.api.v1.level.domain.model.factory.MembershipLevelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisteredUserEventHandlerImpl implements RegisteredUserEventHandler {

  private final RegisterUserLevelPort registerUserLevelPort;
  private final ExistsUserLevelPort existsUserLevelPort;
  private final RegisteredUserSaga registeredUserSaga;

  @Override
  public void handleCreateUserLevel(CreateUserLevelCommand command) {
//    existsUserLevelPort.existsUserLevel(command.eventId());
    final MembershipLevel membershipLevel = MembershipLevelFactory.of(command.userId(), command.createdAt());
    try {
      registeredUserSaga.process(membershipLevel);
    } catch (Exception e) {
      registeredUserSaga.rollback(membershipLevel);
    }
  }
}
