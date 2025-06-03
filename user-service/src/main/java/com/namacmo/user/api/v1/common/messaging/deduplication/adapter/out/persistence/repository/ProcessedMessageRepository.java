package com.namacmo.user.api.v1.common.messaging.deduplication.adapter.out.persistence.repository;

import com.namacmo.user.api.v1.common.messaging.deduplication.adapter.out.persistence.entity.ProcessedMessage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, UUID> {
}
