package com.namacmo.user.api.v1.point.adapter.out.persistence.entity;

import com.namacmo.user.api.v1.point.domain.model.PointType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reward_points_history")
public class RewardPointHistoryJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pointId;
  @Column(nullable = false, name = "user_id")
  private Long userId;
  @Column(nullable = false, name = "related_order_id")
  private String relatedOrderId;
  @Column(nullable = false, name = "point")
  private String point;
  @Column(nullable = false, name = "type")
  @Enumerated(value = EnumType.STRING)
  private PointType pointType;
  @Column(name = "reason")
  private String reason;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardPointHistoryJpaEntity that = (RewardPointHistoryJpaEntity) o;
    return Objects.equals(pointId, that.pointId) && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pointId, userId);
  }
}
