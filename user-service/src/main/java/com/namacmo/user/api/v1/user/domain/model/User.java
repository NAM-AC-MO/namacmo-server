package com.namacmo.user.api.v1.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
  private final UserNo userNo;
  private final UserProfile userProfile;
  private final Roles roles;

  @Builder
  private User(
      UserNo userNo,
      UserProfile userProfile,
      Roles roles
  ) {
    this.userNo = userNo;
    this.userProfile = userProfile;
    this.roles = roles;
  }
}
