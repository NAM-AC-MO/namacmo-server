package com.namacmo.user.api.v1.domain.model;

import com.namacmo.user.api.v1.domain.LevelType;
import com.namacmo.user.api.v1.domain.Money;

public class MembershipLevel {
  private String userLevelId;
  private LevelType levelType;
  private Money totalSpent;
  private Period membershipPeriod;
}
