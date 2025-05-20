package com.namacmo.user.api.v1.user.application.port.in;

import com.namacmo.user.api.v1.user.domain.model.User;

public interface RegisterUserUseCase {
  User registerUser(RegisterUserCommand command);
}
