package com.namacmo.user.api.v1.common.outbox.application.port.out;

import java.util.UUID;

public interface UpdateOutboxEventStatusPort {
  void markOutboxEventSent(UUID eventId);
  void markOutboxEventFailed(UUID eventId);
}
