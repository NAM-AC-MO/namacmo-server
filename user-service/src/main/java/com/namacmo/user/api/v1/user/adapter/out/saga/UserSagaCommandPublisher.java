package com.namacmo.user.api.v1.user.adapter.out.saga;

import com.namacmo.user.api.v1.user.adapter.out.saga.command.UserRegisteredMessage;

public interface UserSagaCommandPublisher {
  void publish(UserRegisteredMessage message);
}
