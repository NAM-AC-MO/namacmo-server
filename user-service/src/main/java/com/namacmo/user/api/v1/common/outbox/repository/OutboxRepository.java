package com.namacmo.user.api.v1.common.outbox.repository;

import com.namacmo.user.api.v1.common.outbox.entity.OutboxEvent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
}
