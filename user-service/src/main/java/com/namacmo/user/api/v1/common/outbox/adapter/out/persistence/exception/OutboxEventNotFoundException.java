package com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.exception;

import java.util.UUID;

public class OutboxEventNotFoundException extends RuntimeException {

  public OutboxEventNotFoundException(String message) {
    super(message);
  }

  public OutboxEventNotFoundException(UUID eventId) {
    super("Outbox event not found with ID: " + eventId);
  }

  public OutboxEventNotFoundException() {
    super("Outbox event not found.");
  }
}
