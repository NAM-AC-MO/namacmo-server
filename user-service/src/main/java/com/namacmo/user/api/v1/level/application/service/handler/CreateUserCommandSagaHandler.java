package com.namacmo.user.api.v1.level.application.service.handler;

import com.namacmo.user.api.v1.level.application.port.in.CreateUserCommandEvent;
import com.namacmo.user.api.v1.level.application.port.in.CreateUserCommandHandler;
import com.namacmo.user.api.v1.level.application.port.out.ExistsUserLevelPort;
import com.namacmo.user.api.v1.level.application.port.out.RegisterUserLevelPort;
import com.namacmo.user.api.v1.level.adapter.out.saga.RegisteredUserSaga;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import com.namacmo.user.api.v1.level.domain.model.factory.MembershipLevelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserCommandSagaHandler implements CreateUserCommandHandler {

  private final ExistsUserLevelPort existsUserLevelPort;
  private final RegisteredUserSaga registeredUserSaga;

  @Override
  public void handle(CreateUserCommandEvent commandEvent) {
    // 메시지 중복체크 existsUserLevelPort.existsUserLevel(command.eventId());
    final MembershipLevel membershipLevel = MembershipLevelFactory.of(commandEvent.userId(), commandEvent.createdAt());
    try {
      registeredUserSaga.process(membershipLevel);
    } catch (Exception e) {
      registeredUserSaga.rollback(membershipLevel);
    }
  }
}
