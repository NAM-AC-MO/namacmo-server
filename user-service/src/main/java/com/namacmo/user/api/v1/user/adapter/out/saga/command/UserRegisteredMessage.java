package com.namacmo.user.api.v1.user.adapter.out.saga.command;

import java.time.LocalDateTime;

public record UserRegisteredMessage(String userId, LocalDateTime createdAt) {
}
