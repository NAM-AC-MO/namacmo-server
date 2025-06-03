package com.namacmo.user.api.v1.level.application.port.in;

import com.namacmo.user.api.v1.level.adapter.in.saga.command.MembershipCommandEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserCommandEvent(
    UUID eventId,
    String userId,
    String channelId,
    LocalDate eventCreatedDate
) implements MembershipCommandEvent {
}
