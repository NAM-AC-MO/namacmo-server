package com.namacmo.user.api.v1.user.domain.model.factory;

import com.namacmo.user.api.v1.user.domain.valueobject.Address;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;

public final class UserProfileFactory {
  private UserProfileFactory() {}

  public static UserProfile of(
      String street,
      String detail,
      String city,
      String zip,
      String email,
      String name,
      String phone
  ) {
    final Address address = AddressFactory.of(street, detail, city, zip);
    return UserProfile.builder()
        .address(address)
        .email(email)
        .name(name)
        .phone(phone)
        .build();
  }
}

