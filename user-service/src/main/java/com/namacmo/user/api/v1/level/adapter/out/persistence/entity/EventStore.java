package com.namacmo.user.api.v1.level.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventStore {
  @Id
  private UUID eventId;
  private String userId;
  private LocalDateTime eventCreatedAt;
  private LocalDateTime processedAt;
}
