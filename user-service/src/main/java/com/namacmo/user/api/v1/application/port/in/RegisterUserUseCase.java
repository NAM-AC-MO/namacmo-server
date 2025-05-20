package com.namacmo.user.api.v1.application.port.in;

import com.namacmo.user.api.v1.domain.model.User;

public interface RegisterUserUseCase {
  User registerUser(RegisterUserCommand command);
}
