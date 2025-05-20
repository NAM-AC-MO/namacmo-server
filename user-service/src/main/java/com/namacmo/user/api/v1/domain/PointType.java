package com.namacmo.user.api.v1.domain;

import lombok.Getter;

@Getter
public enum PointType {
  REWARD("지급"),
  USE("사용"),
  EXPIRE("소멸");

  private final String description;
  PointType(String description) {
    this.description = description;
  }
}
