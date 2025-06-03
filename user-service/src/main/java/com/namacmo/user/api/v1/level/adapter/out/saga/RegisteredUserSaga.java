package com.namacmo.user.api.v1.level.adapter.out.saga;

import com.namacmo.infracommon.saga.SagaStep;
import com.namacmo.user.api.v1.level.application.port.out.RegisterUserLevelPort;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisteredUserSaga implements SagaStep<MembershipLevel> {

  private final RegisterUserLevelPort registerUserLevelPort;

  @Override
  public void process(MembershipLevel data) {
    registerUserLevelPort.createUserLevel(data);
  }

  @Override
  public void rollback(MembershipLevel data) {

  }
}
