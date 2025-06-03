package com.namacmo.user.api.v1.common.outbox.entity;

import com.namacmo.user.api.v1.common.outbox.valueobject.EventType;
import com.namacmo.user.api.v1.common.outbox.valueobject.OutboxType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {
  @Id
  @UuidGenerator
  @JdbcTypeCode(Types.VARCHAR)
  @Column(length = 36, nullable = false, unique = true)
  private UUID id;
  @Column(nullable = false)
  private String aggregateType;
  @Column(nullable = false)
  private String aggregateId;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EventType eventType;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OutboxType outboxType;
  @Column(columnDefinition = "TEXT")
  private String payload;
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Builder
  private OutboxEvent(
      UUID id,
      String aggregateType,
      String aggregateId,
      EventType eventType,
      OutboxType outboxType,
      String payload,
      LocalDateTime createdAt
  ) {
    this.id = id;
    this.aggregateType = aggregateType;
    this.aggregateId = aggregateId;
    this.eventType = eventType;
    this.outboxType = outboxType;
    this.payload = payload;
    this.createdAt = createdAt;
  }
}
