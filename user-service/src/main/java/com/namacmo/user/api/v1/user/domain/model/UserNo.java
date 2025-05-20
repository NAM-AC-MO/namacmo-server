package com.namacmo.user.api.v1.user.domain.model;

import lombok.Getter;

@Getter
public final class UserNo {
  private String userNo;

  public UserNo(String userId) {
    this.userNo = userId;
  }
}
