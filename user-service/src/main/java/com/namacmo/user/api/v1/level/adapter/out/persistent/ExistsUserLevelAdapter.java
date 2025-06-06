package com.namacmo.user.api.v1.level.adapter.out.persistent;

import com.namacmo.appcommon.hexagonal.WebAdapter;
import com.namacmo.user.api.v1.level.adapter.out.persistent.repository.UserLevelJpaRepository;
import com.namacmo.user.api.v1.level.application.port.out.ExistsUserLevelPort;
import com.namacmo.user.api.v1.level.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;

@WebAdapter
@RequiredArgsConstructor
public class ExistsUserLevelAdapter implements ExistsUserLevelPort {

  private final UserLevelJpaRepository userLevelJpaRepository;

  @Override
  public boolean existsUserLevel(UserId userId) {
    return userLevelJpaRepository.existsByUserId(userId.userId());
  }
}
