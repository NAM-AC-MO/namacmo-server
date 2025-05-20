package com.namacmo.user.api.v1.adapter.out.persistence;

import com.namacmo.user.api.v1.domain.LevelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;
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
  @Enumerated(value = EnumType.STRING)
  @ManyToOne
  private UserJpaEntity user;
  @Column(name = "level_type")
  private LevelType levelType;
  @Column(name = "total_spent")
  private BigInteger totalSpent;
  @Column(name = "start_date")
  private LocalDateTime startDate;
  @Column(name = "end_date")
  private LocalDateTime endDate;

  // 연관관계 설정 메서드
  public void setUserJpaEntity(UserJpaEntity user) {
    this.user = user;
  }
}
