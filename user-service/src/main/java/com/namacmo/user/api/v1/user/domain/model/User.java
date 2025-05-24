package com.namacmo.user.api.v1.user.domain.model;

import com.namacmo.user.api.v1.user.domain.valueobject.Role;
import com.namacmo.user.api.v1.user.domain.valueobject.Roles;
import com.namacmo.user.api.v1.user.domain.valueobject.UserId;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

public class User {
  @Getter
  private final UserId userId;
  @Getter
  private final UserProfile userProfile;
  private final Roles roles;

  public Set<Role> getRoles() {
    return roles.getRoles();
  }

  @Builder
  private User(
      UserId userId,
      UserProfile userProfile,
      Roles roles
  ) {
    this.userId = userId;
    this.userProfile = userProfile;
    this.roles = roles;
  }
}
