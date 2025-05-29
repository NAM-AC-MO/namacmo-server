package com.namacmo.user.api.v1.level.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserLevelCommand(
    UUID eventId,
    String userId,
    String channelId,
    LocalDateTime createdAt
) {
}
