package com.namacmo.user.api.v1.level.domain.model;

import com.namacmo.user.api.v1.common.Money;

public class MembershipLevel {
  private String userLevelId;
  private LevelType levelType;
  private Money totalSpent;
  private Period membershipPeriod;
}
