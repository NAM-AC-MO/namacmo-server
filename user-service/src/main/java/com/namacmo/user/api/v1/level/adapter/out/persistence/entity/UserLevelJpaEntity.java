package com.namacmo.user.api.v1.level.adapter.out.persistence.entity;

import com.namacmo.user.api.v1.level.domain.model.LevelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_levels")
public class UserLevelJpaEntity {
  @Id
  private Long userLevelId;
  @Column(nullable = false, name = "user_id")
  private Long userId;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "level_type")
  private LevelType levelType;
  @Column(name = "total_spent")
  private BigInteger totalSpent;
  @Column(name = "start_date")
  private LocalDateTime startDate;
  @Column(name = "end_date")
  private LocalDateTime endDate;

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
