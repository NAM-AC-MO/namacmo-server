package com.namacmo.user.api.v1.user.domain.valueobject;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public final class Address {
  private final String streetAddress;
  private final String detailAddress;
  private final String city;
  private final String zipCode;

  @Builder
  private Address(
      String streetAddress,
      String detailAddress,
      String city,
      String zipCode
  ) {
    this.streetAddress = streetAddress;
    this.detailAddress = detailAddress;
    this.city = city;
    this.zipCode = zipCode;
    Assert.hasText(streetAddress, "기본주소 정보가 올바르지 않습니다.");
    Assert.hasText(detailAddress, "상세주소 정보가 올바르지 않습니다.");
    Assert.hasText(city, "도시/지역 정보가 올바르지 않습니다.");
    Assert.hasText(zipCode, "우편번호 정보가 올바르지 않습니다.");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return Objects.equals(streetAddress, address.streetAddress) && Objects.equals(detailAddress,
        address.detailAddress) && Objects.equals(city, address.city) && Objects.equals(zipCode,
        address.zipCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(streetAddress, detailAddress, city, zipCode);
  }
}
