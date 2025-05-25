package com.namacmo.user.api.v1.user.domain.model.factory;

import com.namacmo.user.api.v1.user.domain.model.User;
import com.namacmo.user.api.v1.user.domain.valueobject.Roles;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;

public final class UserFactory {
  private UserFactory() {}

  public static User createUser(
      String streetAddress,
      String detailAddress,
      String city,
      String zipCode,
      String email,
      String name,
      String phone
  ) {
    final UserProfile profile = UserProfileFactory.of(streetAddress, detailAddress, city, zipCode, email, name, phone);
    final Roles roles = RolesFactory.of();

    return User.create(profile, roles);
  }
}

