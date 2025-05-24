package com.namacmo.user.api.v1.user.domain.valueobject;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class UserProfile {
  private Address address;
  private String email;
  private String name;
  private String phone;

  @Builder
  private UserProfile(
      Address address,
      String email,
      String name,
      String phone
  ) {
    this.address = address;
    this.email = email;
    this.name = name;
    this.phone = phone;
    Assert.notNull(address, "주소 정보가 올바르지 않습니다.");
    Assert.hasText(email, "사용자 정보가 올바르지 않습니다.");
    Assert.hasText(name, "사용자 정보가 올바르지 않습니다.");
    Assert.hasText(phone, "사용자 정보가 올바르지 않습니다.");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProfile that = (UserProfile) o;
    return Objects.equals(address, that.address) && Objects.equals(email, that.email)
        && Objects.equals(name, that.name) && Objects.equals(phone, that.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, email, name, phone);
  }
}
