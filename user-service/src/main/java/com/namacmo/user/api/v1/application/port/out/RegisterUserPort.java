package com.namacmo.user.api.v1.application.port.out;

import com.namacmo.user.api.v1.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.domain.model.UserProfile;

public interface RegisterUserPort {
  UserJpaEntity registerUser(UserProfile userProfile);
}
