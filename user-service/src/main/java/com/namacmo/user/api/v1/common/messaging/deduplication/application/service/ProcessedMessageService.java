package com.namacmo.user.api.v1.common.messaging.deduplication.application.service;

import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.in.MessageDeduplicationUseCase;
import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.out.MessageDeduplicationCheckPort;
import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.out.MessageProcessingMarkPort;
import jakarta.persistence.EntityExistsException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProcessedMessageService implements MessageDeduplicationUseCase {
  private final MessageDeduplicationCheckPort messageDeduplicationCheckPort;
  private final MessageProcessingMarkPort messageProcessingMarkPort;

  @Override
  @Transactional(readOnly = true)
  public boolean isProcessed(UUID messageId) {
    return messageDeduplicationCheckPort.isProcessed(messageId);
  }

  @Override
  @Transactional
  public void markProcessed(UUID messageId) {
    try {
      messageProcessingMarkPort.markProcessed(messageId);
    } catch (DataIntegrityViolationException e) {
      throw new EntityExistsException("Message already processed: " + messageId);
    }
  }
}