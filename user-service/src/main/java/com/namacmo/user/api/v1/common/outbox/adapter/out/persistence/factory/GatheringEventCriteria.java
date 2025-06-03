package com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.factory;

import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.EventType;
import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.valueobject.OutboxType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class GatheringEventCriteria {
  private final UUID eventId;
  private final String aggregateType;
  private final String aggregateId;
  private final EventType eventType;
  private final OutboxType outboxType;
  private final String payload;
  private final LocalDateTime createdAt;

  @Builder
  private GatheringEventCriteria(
      UUID eventId,
      String aggregateType,
      String aggregateId,
      EventType eventType,
      OutboxType outboxType,
      String payload,
      LocalDateTime createdAt
  ) {
    this.eventId = eventId;
    this.aggregateType = aggregateType;
    this.aggregateId = aggregateId;
    this.eventType = eventType;
    this.outboxType = outboxType;
    this.payload = payload;
    this.createdAt = createdAt;

    Assert.notNull(aggregateType, "aggregateType이 올바르지 않습니다.");
    Assert.notNull(aggregateId, "aggregateId가 올바르지 않습니다.");
    Assert.notNull(eventType, "eventType이 올바르지 않습니다.");
    Assert.notNull(outboxType, "outboxType이 올바르지 않습니다.");
    Assert.notNull(createdAt, "createdAt이 올바르지 않습니다.");
  }
}
