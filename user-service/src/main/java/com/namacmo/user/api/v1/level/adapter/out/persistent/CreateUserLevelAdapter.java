package com.namacmo.user.api.v1.level.adapter.out.persistent;

import com.namacmo.appcommon.hexagonal.PersistenceAdapter;
import com.namacmo.user.api.v1.level.adapter.out.persistent.entity.UserLevelJpaEntity;
import com.namacmo.user.api.v1.level.adapter.out.persistent.mapper.CreateUserLevelMapper;
import com.namacmo.user.api.v1.level.adapter.out.persistent.repository.UserLevelJpaRepository;
import com.namacmo.user.api.v1.level.application.port.out.RegisterUserLevelPort;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateUserLevelAdapter implements RegisterUserLevelPort {

  private final UserLevelJpaRepository userLevelJpaRepository;
  private final CreateUserLevelMapper mapper;

  @Override
  public UserLevelJpaEntity createUserLevel(MembershipLevel membershipLevel) {
    final UserLevelJpaEntity userLevelJpaEntity = mapper.mapToJpaEntity(membershipLevel);
    return userLevelJpaRepository.save(userLevelJpaEntity);
  }
}
