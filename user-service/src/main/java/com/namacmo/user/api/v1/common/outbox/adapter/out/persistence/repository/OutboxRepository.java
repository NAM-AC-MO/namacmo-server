package com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.repository;

import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.entity.OutboxEvent;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.OutboxType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
  List<OutboxEvent> findAllByOutboxType(OutboxType outboxType, Pageable pageable);
}
