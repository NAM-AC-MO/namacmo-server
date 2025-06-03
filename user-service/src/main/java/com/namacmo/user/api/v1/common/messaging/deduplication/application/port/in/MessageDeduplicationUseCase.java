package com.namacmo.user.api.v1.common.messaging.deduplication.application.port.in;

import java.util.UUID;

public interface MessageDeduplicationUseCase {
  boolean isProcessed(UUID messageId);
  void markProcessed(UUID messageId);
}
