package com.namacmo.user.api.v1.level.application.service.message;

import com.namacmo.user.api.v1.common.Money;
import com.namacmo.user.api.v1.level.application.port.out.RegisterUserLevelPort;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import com.namacmo.user.api.v1.level.domain.model.factory.MembershipLevelFactory;
import com.namacmo.user.api.v1.level.domain.valueobject.LevelType;
import com.namacmo.user.api.v1.level.domain.valueobject.Period;
import com.namacmo.user.api.v1.level.domain.valueobject.UserId;
import com.namacmo.user.api.v1.level.domain.valueobject.UserLevelId;
import com.namacmo.user.api.v1.point.adapter.out.persistence.repository.RewardPointHistoryJpaRepository;
import com.namacmo.user.api.v1.level.application.dto.CreateUserLevelCommand;
import com.namacmo.user.api.v1.level.application.port.in.RegisteredUserRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegisteredUserRequestMessageListenerImpl implements RegisteredUserRequestMessageListener {

  private final RegisterUserLevelPort registerUserLevelPort;

  @Override
  public MembershipLevel createUserLevel(CreateUserLevelCommand command) {
    log.info("userId={} channelId={} createAt={}", command.userId(), command.channelId(), command.createdAt());

    final MembershipLevel membershipLevel = MembershipLevelFactory.of(command.userId(), command.createdAt());
    registerUserLevelPort.createUserLevel(membershipLevel);

    return membershipLevel;
  }
}
