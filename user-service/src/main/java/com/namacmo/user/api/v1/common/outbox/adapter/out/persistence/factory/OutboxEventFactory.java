package com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.factory;

import com.namacmo.user.api.v1.common.outbox.adapter.out.persistence.entity.OutboxEvent;

public final class OutboxEventFactory {
  private OutboxEventFactory() {
    throw new IllegalStateException();
  }

  public static OutboxEvent create(GatheringEventCriteria criteria) {
    return OutboxEvent.builder()
        .eventId(criteria.getEventId())
        .aggregateType(criteria.getAggregateType())
        .aggregateId(criteria.getAggregateId())
        .eventType(criteria.getEventType())
        .outboxType(criteria.getOutboxType())
        .payload(criteria.getPayload())
        .createdAt(criteria.getCreatedAt())
        .build();
  }

}
