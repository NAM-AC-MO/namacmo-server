package com.namacmo.user.api.v1.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterUserCommand {
  private final String streetAddress;
  private final String detailAddress;
  private final String city;
  private final String zipCode;
  private final String email;
  private final String name;
  private final String phone;

  @Builder
  private RegisterUserCommand(
      String streetAddress,
      String detailAddress,
      String city,
      String zipCode,
      String email,
      String name,
      String phone
  ) {
    this.streetAddress = streetAddress;
    this.detailAddress = detailAddress;
    this.city = city;
    this.zipCode = zipCode;
    this.email = email;
    this.name = name;
    this.phone = phone;
  }
}
