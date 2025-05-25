package com.namacmo.user.api.v1.level.domain.valueobject;

import java.time.LocalDateTime;

public record Period(
    LocalDateTime startDate,
    LocalDateTime endDate
) {
  private static final long STANDARD_MONTHS = 6L;

  public Period {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("시작일과 종료일을 입력해주세요.");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("시작일은 종료일보다 이후일 수 없습니다.");
    }
  }

  public Period(LocalDateTime now) {
    this(
        now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0),
        now.plusMonths(STANDARD_MONTHS)
            .withDayOfMonth(1)
            .minusDays(1)
            .withHour(23).withMinute(59).withSecond(59).withNano(999_999_999)
    );
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
