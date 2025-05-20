package com.namacmo.user.api.v1.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressJpaVo {
  @Column(name = "street_address")
  private String streetAddress;
  @Column(name = "detail_address")
  private String detailAddress;
  @Column(name = "city")
  private String city;
  @Column(name = "zip_code")
  private String zipCode;
}
