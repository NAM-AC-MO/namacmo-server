package com.namacmo.user.api.v1.domain.model;

import com.namacmo.user.api.v1.domain.Money;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RewardPoints {
  private final List<RewardPoint> rewardPoints = new ArrayList<>();

  public RewardPoints(List<RewardPoint> rewardPoints) {
    this.rewardPoints.addAll(rewardPoints);
  }

  public Money getPointBalance() {
    return rewardPoints.stream()
        .map(RewardPoint::getPoint)
        .reduce(Money::add)
        .orElse(Money.ZERO);
  }

  public List<RewardPoint> getRewardPoints() {
    return Collections.unmodifiableList(rewardPoints);
  }

}
