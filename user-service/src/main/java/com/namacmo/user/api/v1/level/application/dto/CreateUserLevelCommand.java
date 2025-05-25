package com.namacmo.user.api.v1.level.application.dto;

import java.time.LocalDateTime;

public record CreateUserLevelCommand(
    String userId,
    String channelId,
    LocalDateTime createdAt
) {
}
