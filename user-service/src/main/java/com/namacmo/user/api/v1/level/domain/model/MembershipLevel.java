package com.namacmo.user.api.v1.level.domain.model;

import com.namacmo.appcommon.domain.entity.AggregateRoot;
import com.namacmo.user.api.v1.common.Money;
import com.namacmo.user.api.v1.level.domain.valueobject.LevelType;
import com.namacmo.user.api.v1.level.domain.valueobject.Period;
import com.namacmo.user.api.v1.level.domain.valueobject.UserId;
import com.namacmo.user.api.v1.level.domain.valueobject.UserLevelId;
import java.time.LocalDate;
import lombok.Builder;

public class MembershipLevel extends AggregateRoot<MembershipLevel, UserLevelId> {
  private UserId userId;
  private LevelType levelType;
  private Money totalSpent;
  private Period membershipPeriod;

  @Builder
  private MembershipLevel(
      UserLevelId userLevelId,
      UserId userId,
      LevelType levelType,
      Money totalSpent,
      Period membershipPeriod
  ) {
    super(userLevelId);
    this.userId = userId;
    this.levelType = levelType;
    this.totalSpent = totalSpent;
    this.membershipPeriod = membershipPeriod;
  }

  public String getUserId() {
    return userId.userId();
  }

  public LevelType getLevelType() {
    return levelType;
  }

  public Money getTotalSpent() {
    return totalSpent;
  }

  public LocalDate getStartDate() {
    return membershipPeriod.startDate();
  }

  public LocalDate getEndDate() {
    return membershipPeriod.endDate();
  }
}
