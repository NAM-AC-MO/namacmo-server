package com.namacmo.user.api.v1.user.adapter.out.saga.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegisteredMessage(
    UUID eventId,
    String userId,
    LocalDateTime createdAt
) {
}
