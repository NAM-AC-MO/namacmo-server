package com.namacmo.user.api.v1.user.application.service;

import com.namacmo.appcommon.UseCase;
import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.adapter.out.persistence.mapper.RegisterUserMapper;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserCommand;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserUseCase;
import com.namacmo.user.api.v1.user.application.port.out.RegisterUserPort;
import com.namacmo.user.api.v1.user.domain.model.Address;
import com.namacmo.user.api.v1.user.domain.model.User;
import com.namacmo.user.api.v1.user.domain.model.UserProfile;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

  private final RegisterUserPort registerUserPort;
  private final RegisterUserMapper mapper;

  @Override
  public User registerUser(RegisterUserCommand command) {
    final Address address = Address.builder()
        .streetAddress(command.getStreetAddress())
        .detailAddress(command.getDetailAddress())
        .city(command.getCity())
        .zipCode(command.getZipCode())
        .build();

    final UserProfile userProfile = UserProfile.builder()
        .address(address)
        .email(command.getEmail())
        .name(command.getName())
        .phone(command.getPhone())
        .build();

    final UserJpaEntity userJpaEntity = registerUserPort.registerUser(userProfile);
    return mapper.mapToDomain(userJpaEntity);
  }

}
