package com.namacmo.user.api.v1.domain.model;

import com.namacmo.user.api.v1.domain.Money;
import com.namacmo.user.api.v1.domain.PointType;

public class RewardPoint {
  private String pointId;
  private String relatedOrderId;
  private Money point;
  private PointType pointType;
  private String reason;

  public Money getPoint() {
    return point;
  }
}
