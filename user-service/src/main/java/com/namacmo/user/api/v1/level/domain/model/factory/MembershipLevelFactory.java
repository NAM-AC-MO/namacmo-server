package com.namacmo.user.api.v1.level.domain.model.factory;

import com.namacmo.user.api.v1.common.Money;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import com.namacmo.user.api.v1.level.domain.valueobject.LevelType;
import com.namacmo.user.api.v1.level.domain.valueobject.Period;
import com.namacmo.user.api.v1.level.domain.valueobject.UserId;
import com.namacmo.user.api.v1.level.domain.valueobject.UserLevelId;
import java.time.LocalDateTime;

public final class MembershipLevelFactory {
  private MembershipLevelFactory() {
  }

  public static MembershipLevel of(
      String userId,
      LocalDateTime createdAt
  ) {
    return MembershipLevel.builder()
        .userLevelId(new UserLevelId())
        .userId(new UserId(userId))
        .levelType(LevelType.GREEN)
        .totalSpent(Money.ZERO)
        .membershipPeriod(new Period(createdAt))
        .build();
  }
}
