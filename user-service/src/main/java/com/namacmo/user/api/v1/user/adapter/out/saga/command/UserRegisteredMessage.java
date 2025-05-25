package com.namacmo.user.api.v1.user.adapter.out.saga.command;

import java.time.Instant;

public record UserRegisteredMessage(String userId, Instant createdAt) {
}
