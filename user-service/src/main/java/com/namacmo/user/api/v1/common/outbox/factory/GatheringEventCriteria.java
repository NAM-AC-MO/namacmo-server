package com.namacmo.user.api.v1.common.outbox.factory;

import com.namacmo.user.api.v1.common.outbox.valueobject.EventType;
import com.namacmo.user.api.v1.common.outbox.valueobject.OutboxType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class GatheringEventCriteria {
  private final String aggregateType;     // 예: "Order"
  private final String aggregateId;       // 예: "order-123"
  private final EventType eventType;      // 예: EventType.CREATED
  private final OutboxType outboxType;    // 예: OutboxType.ORDER
  private final String payload;           // JSON 직렬화된 실제 내용 (또는 null)
  private final LocalDateTime createdAt;  // 이벤트 생성 시간

  @Builder
  private GatheringEventCriteria(
      String aggregateType,
      String aggregateId,
      EventType eventType,
      OutboxType outboxType,
      String payload,
      LocalDateTime createdAt
  ) {
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
