package com.namacmo.user.api.v1.user.adapter.out.persistent;

import com.namacmo.appcommon.hexagonal.PersistenceAdapter;
import com.namacmo.user.api.v1.user.adapter.out.persistent.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.adapter.out.persistent.mapper.RegisterUserMapper;
import com.namacmo.user.api.v1.user.adapter.out.persistent.repository.UserJpaRepository;
import com.namacmo.user.api.v1.user.application.exception.UserAlreadyExistsException;
import com.namacmo.user.api.v1.user.application.port.out.RegisterUserPort;
import com.namacmo.user.api.v1.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegisterUserAdapter implements RegisterUserPort {

  private final UserJpaRepository userJpaRepository;
  private final RegisterUserMapper mapper;

  @Override
  public UserJpaEntity registerUser(User user) {
    final UserJpaEntity userJpaEntity = mapper.mapToJpaEntity(user);
    userJpaRepository.findByEmail(userJpaEntity.getEmail())
        .ifPresent(findUser -> {
          throw new UserAlreadyExistsException(findUser.getEmail());
        });
    return userJpaRepository.save(userJpaEntity);
  }
}
