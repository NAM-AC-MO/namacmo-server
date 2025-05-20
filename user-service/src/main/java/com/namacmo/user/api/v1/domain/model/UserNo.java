package com.namacmo.user.api.v1.domain.model;

import lombok.Getter;

@Getter
public final class UserNo {
  private String userNo;

  public UserNo(String userId) {
    this.userNo = userId;
  }
}
