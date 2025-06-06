package com.namacmo.user.api.v1.user.application.service;

import com.namacmo.appcommon.hexagonal.UseCase;
import com.namacmo.user.api.v1.user.adapter.out.persistent.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.adapter.out.persistent.mapper.RegisterUserMapper;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserCommand;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserUseCase;
import com.namacmo.user.api.v1.user.application.port.out.RegisterUserPort;
import com.namacmo.user.api.v1.user.domain.model.User;
import com.namacmo.user.api.v1.user.domain.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class OutboxRegisterUserService implements RegisterUserUseCase {

  private final OutboxUserDomainEventPublisher outboxUserDomainEventPublisher;
  private final RegisterUserPort registerUserPort;
  private final RegisterUserMapper mapper;

  @Override
  @Transactional
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
    outboxUserDomainEventPublisher.publish(user.getDomainEvents());
    // evert store에 evnet 저장
    user.clearDomainEvents();

    return mapper.mapToDomain(userJpaEntity);
  }

}
