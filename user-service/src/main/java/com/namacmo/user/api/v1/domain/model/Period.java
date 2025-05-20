package com.namacmo.user.api.v1.domain.model;

import java.time.LocalDateTime;

public record Period(
    LocalDateTime startDate,
    LocalDateTime endDate
) {

  public Period {
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("시작일은 종료일보다 이후일 수 없습니다.");
    }
  }

  public boolean isWithin(LocalDateTime target) {
    return target != null && !target.isBefore(startDate) && !target.isAfter(endDate);
  }

  public boolean isExpired(LocalDateTime now) {
    return endDate.isBefore(now);
  }

  public boolean isActive(LocalDateTime now) {
    return isWithin(now);
  }
}
