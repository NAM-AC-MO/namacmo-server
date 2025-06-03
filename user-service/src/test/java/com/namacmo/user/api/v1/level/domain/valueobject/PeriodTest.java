package com.namacmo.user.api.v1.level.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PeriodTest {

  @Test
  @DisplayName("주어진 날짜 기준으로 시작일은 해당 달의 1일 00:00, 종료일은 6개월 뒤 말일 23:59:59.999999999가 되어야 한다")
  void createsPeriodFromFirstDayToEndOfSixthMonth() {
    // given
    LocalDate baseDate = LocalDate.of(2025, 5, 22);

    // when
    Period period = new Period(baseDate);

    // then
    LocalDate expectedStart = LocalDate.of(2025, 5, 1);
    LocalDate expectedEnd = LocalDate.of(2025, 10, 31);

    assertThat(period.startDate())
        .as("기간 시작일은 해당 월의 1일 이어야 한다")
        .isEqualTo(expectedStart);

    assertThat(period.endDate())
        .as("기간 종료일은 6개월 후 말일 이어야 한다")
        .isEqualTo(expectedEnd);
  }
}