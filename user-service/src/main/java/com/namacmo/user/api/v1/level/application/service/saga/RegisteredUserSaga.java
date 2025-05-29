package com.namacmo.user.api.v1.level.application.service.saga;

import com.namacmo.infracommon.saga.SagaStep;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import org.springframework.stereotype.Component;

@Component
public class RegisteredUserSaga implements SagaStep<MembershipLevel> {
  @Override
  public void process(MembershipLevel data) {

  }

  @Override
  public void rollback(MembershipLevel data) {

  }
}
