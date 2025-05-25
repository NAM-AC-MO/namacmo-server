package com.namacmo.user.api.v1.level.adapter.out.persistence.mapper;

import com.namacmo.user.api.v1.level.adapter.out.persistence.entity.UserLevelJpaEntity;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;
import org.springframework.stereotype.Component;

@Component
public class CreateUserLevelMapper {
  public UserLevelJpaEntity mapToJpaEntity(MembershipLevel membershipLevel) {
    return UserLevelJpaEntity.builder()
        .userLevelId(membershipLevel.getId().getValue())
        .userId(membershipLevel.getUserId())
        .levelType(membershipLevel.getLevelType())
        .totalSpent(membershipLevel.getTotalSpent())
        .startDate(membershipLevel.getStartDate())
        .endDate(membershipLevel.getEndDate())
        .build();
  }
}
