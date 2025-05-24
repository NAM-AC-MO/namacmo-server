package com.namacmo.user.api.v1.user.domain.model.factory;

import com.namacmo.user.api.v1.user.domain.valueobject.Address;

public final class AddressFactory {
  private AddressFactory() {}

  public static Address of(
      String streetAddress,
      String detailAddress,
      String city,
      String zipCode
  ) {
    return Address.builder()
        .streetAddress(streetAddress)
        .detailAddress(detailAddress)
        .city(city)
        .zipCode(zipCode)
        .build();
  }
}

