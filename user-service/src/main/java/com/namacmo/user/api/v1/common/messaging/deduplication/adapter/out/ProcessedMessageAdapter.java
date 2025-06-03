package com.namacmo.user.api.v1.common.messaging.deduplication.adapter.out;

import com.namacmo.appcommon.hexagonal.PersistenceAdapter;
import com.namacmo.user.api.v1.common.messaging.deduplication.adapter.out.persistence.entity.ProcessedMessage;
import com.namacmo.user.api.v1.common.messaging.deduplication.adapter.out.persistence.repository.ProcessedMessageRepository;
import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.out.MessageDeduplicationCheckPort;
import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.out.MessageProcessingMarkPort;
import jakarta.persistence.EntityExistsException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProcessedMessageAdapter implements MessageDeduplicationCheckPort, MessageProcessingMarkPort {
  private final ProcessedMessageRepository repository;
  @Override
  public boolean isProcessed(UUID messageId) {
    return repository.existsById(messageId);
  }

  @Override
  public void markProcessed(UUID messageId) {
    try {
      repository.save(new ProcessedMessage(messageId));
    } catch (DataIntegrityViolationException e) {
      throw new EntityExistsException("Message already processed: " + messageId);
    }
  }
}
