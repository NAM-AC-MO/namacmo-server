package com.namacmo.user.api.v1.level.adapter.out.persistence.entity;

import com.namacmo.user.api.v1.common.Money;
import com.namacmo.user.api.v1.level.adapter.out.persistence.MoneyToBigIntegerConvert;
import com.namacmo.user.api.v1.level.domain.valueobject.LevelType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_levels")
public class UserLevelJpaEntity {

  @Id
  private String userLevelId;
  @Column(nullable = false, name = "user_id")
  private String userId;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "level_type")
  private LevelType levelType;
  @Column(name = "total_spent")
  @Convert(converter = MoneyToBigIntegerConvert.class)
  private Money totalSpent;
  @Column(name = "start_date")
  private LocalDate startDate;
  @Column(name = "end_date")
  private LocalDate endDate;

  @Builder
  private UserLevelJpaEntity(
      String userLevelId,
      String userId,
      LevelType levelType,
      Money totalSpent,
      LocalDate startDate,
      LocalDate endDate
  ) {
    this.userLevelId = userLevelId;
    this.userId = userId;
    this.levelType = levelType;
    this.totalSpent = totalSpent;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserLevelJpaEntity that = (UserLevelJpaEntity) o;
    return Objects.equals(userLevelId, that.userLevelId) && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userLevelId, userId);
  }
}
