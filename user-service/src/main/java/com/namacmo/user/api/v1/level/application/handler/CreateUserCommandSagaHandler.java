package com.namacmo.user.api.v1.level.application.handler;

import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.in.MessageDeduplicationUseCase;
import com.namacmo.user.api.v1.common.messaging.handler.AbstractDeduplicatedEventHandler;
import com.namacmo.user.api.v1.level.adapter.out.saga.RegisteredUserSaga;
import com.namacmo.user.api.v1.level.application.port.in.CreateUserCommandEvent;
import com.namacmo.user.api.v1.level.application.port.in.CreateUserCommandHandler;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import com.namacmo.user.api.v1.level.domain.model.factory.MembershipLevelFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateUserCommandSagaHandler extends AbstractDeduplicatedEventHandler<CreateUserCommandEvent>
    implements CreateUserCommandHandler {

  private final RegisteredUserSaga registeredUserSaga;

  public CreateUserCommandSagaHandler(
      MessageDeduplicationUseCase messageDeduplicationUseCase,
      RegisteredUserSaga registeredUserSaga
  ) {
    super(messageDeduplicationUseCase);
    this.registeredUserSaga = registeredUserSaga;
  }

  @Override
  public void handle(CreateUserCommandEvent commandEvent) {
    handle(commandEvent, commandEvent.eventId());
  }

  @Override
  protected void process(CreateUserCommandEvent commandEvent) {
    final MembershipLevel membershipLevel = MembershipLevelFactory.of(commandEvent.userId(), commandEvent.eventCreatedDate());
    try {
      registeredUserSaga.process(membershipLevel);
    } catch (Exception e) {
      registeredUserSaga.rollback(membershipLevel);
    }
  }
}
