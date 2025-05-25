package com.namacmo.user.api.v1.point.domain.model;

import com.namacmo.user.api.v1.common.Money;
import com.namacmo.user.api.v1.point.domain.valueobject.PointType;
import java.util.Objects;
import lombok.Builder;

public class RewardPoint {
  private String pointId;
  private String relatedOrderId;
  private Money point;
  private PointType pointType;
  private String reason;

  @Builder
  private RewardPoint(
      String pointId,
      String relatedOrderId,
      Money point,
      PointType pointType,
      String reason
  ) {
    this.pointId = pointId;
    this.relatedOrderId = relatedOrderId;
    this.point = point;
    this.pointType = pointType;
    this.reason = reason;
  }

  public Money getPoint() {
    return point;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardPoint that = (RewardPoint) o;
    return Objects.equals(pointId, that.pointId) && Objects.equals(relatedOrderId,
        that.relatedOrderId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pointId, relatedOrderId);
  }
}
