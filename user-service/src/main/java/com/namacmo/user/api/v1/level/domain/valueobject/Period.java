package com.namacmo.user.api.v1.level.domain.valueobject;

import java.time.LocalDate;

public record Period(
    LocalDate startDate,
    LocalDate endDate
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

  public Period(LocalDate now) {
    this(
        now.withDayOfMonth(1),
        now.plusMonths(STANDARD_MONTHS)
            .withDayOfMonth(1)
            .minusDays(1)
    );
  }

  public boolean isWithin(LocalDate target) {
    return target != null && !target.isBefore(startDate) && !target.isAfter(endDate);
  }

  public boolean isExpired(LocalDate now) {
    return endDate.isBefore(now);
  }

  public boolean isActive(LocalDate now) {
    return isWithin(now);
  }
}
