package com.namacmo.user.api.v1.user.application.port.out;

import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.domain.model.User;

public interface RegisterUserPort {
  UserJpaEntity registerUser(User user);
}
