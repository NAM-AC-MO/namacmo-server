package com.namacmo.user.api.v1.common.messaging.deduplication.application.port.out;

import java.util.UUID;

public interface MessageProcessingMarkPort {
  void markProcessed(UUID messageId);
}
