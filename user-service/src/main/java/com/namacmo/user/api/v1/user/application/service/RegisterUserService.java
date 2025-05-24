package com.namacmo.user.api.v1.user.application.service;

import com.namacmo.appcommon.hexagonal.UseCase;
import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.adapter.out.persistence.mapper.RegisterUserMapper;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserCommand;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserUseCase;
import com.namacmo.user.api.v1.user.application.port.out.RegisterUserPort;
import com.namacmo.user.api.v1.user.domain.model.User;
import com.namacmo.user.api.v1.user.domain.model.factory.UserFactory;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

  private final RegisterUserPort registerUserPort;
  private final RegisterUserMapper mapper;

  @Override
  public User registerUser(RegisterUserCommand command) {
    final User user = UserFactory.createUser(
        command.getStreetAddress(),
        command.getDetailAddress(),
        command.getCity(),
        command.getZipCode(),
        command.getEmail(),
        command.getName(),
        command.getPhone()
    );

    final UserJpaEntity userJpaEntity = registerUserPort.registerUser(user);
    return mapper.mapToDomain(userJpaEntity);
  }

}
