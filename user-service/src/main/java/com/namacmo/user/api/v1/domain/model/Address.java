package com.namacmo.user.api.v1.domain.model;

import org.springframework.util.Assert;

public record Address(
    String streetAddress,
    String detailAddress,
    String city,
    String zipCode
) {
  public Address {
    Assert.hasText(streetAddress, "기본주소 정보가 올바르지 않습니다.");
    Assert.hasText(detailAddress, "상세주소 정보가 올바르지 않습니다.");
    Assert.hasText(city, "도시/지역 정보가 올바르지 않습니다.");
    Assert.hasText(zipCode, "우편번호 정보가 올바르지 않습니다.");
  }
}
