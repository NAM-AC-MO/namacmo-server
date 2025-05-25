package com.namacmo.user.api.v1.level.domain.valueobject;

import org.springframework.util.Assert;

public record UserId(String userId) {
  public UserId {
    Assert.hasText(userId, "사용자 id가 존재하지 않습니다.");
  }
}
