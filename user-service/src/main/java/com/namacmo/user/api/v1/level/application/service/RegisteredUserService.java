package com.namacmo.user.api.v1.level.application.service;

import com.namacmo.user.api.v1.level.application.port.in.CreateUserCommandEvent;
import com.namacmo.user.api.v1.level.application.port.out.RegisterUserLevelPort;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import com.namacmo.user.api.v1.level.domain.model.factory.MembershipLevelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisteredUserService {

  private final RegisterUserLevelPort registerUserLevelPort;

  public MembershipLevel createUserLevel(CreateUserCommandEvent command) {
    log.info("userId={} channelId={} createAt={}", command.userId(), command.channelId(), command.createdAt());

    final MembershipLevel membershipLevel = MembershipLevelFactory.of(command.userId(), command.createdAt());
    registerUserLevelPort.createUserLevel(membershipLevel);

    return membershipLevel;
  }
}
