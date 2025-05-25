package com.namacmo.user.api.v1.level.application.port.out;

import com.namacmo.user.api.v1.level.adapter.out.persistence.entity.UserLevelJpaEntity;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;

public interface RegisterUserLevelPort {
  UserLevelJpaEntity createUserLevel(MembershipLevel membershipLevel);
}
