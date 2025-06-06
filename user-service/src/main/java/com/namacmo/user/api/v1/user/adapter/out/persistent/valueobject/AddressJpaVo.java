package com.namacmo.user.api.v1.user.adapter.out.persistent.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressJpaVo {
  @Column(name = "street_address")
  private String streetAddress;
  @Column(name = "detail_address")
  private String detailAddress;
  @Column(name = "city")
  private String city;
  @Column(name = "zip_code")
  private String zipCode;

  @Builder
  private AddressJpaVo(
      String streetAddress,
      String detailAddress,
      String city,
      String zipCode
  ) {
    this.streetAddress = streetAddress;
    this.detailAddress = detailAddress;
    this.city = city;
    this.zipCode = zipCode;
  }
}
