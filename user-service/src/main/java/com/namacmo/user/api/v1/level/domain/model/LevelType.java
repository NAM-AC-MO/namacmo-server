package com.namacmo.user.api.v1.level.domain.model;

import lombok.Getter;

/**
 * 최근 6개월간 구매 금액 기준으로 산정되는 멤버십 등급 Enum
 */
@Getter
public enum LevelType {

  VIP(
      "VIP",
      "70만원 이상",
      "월 15% 할인쿠폰 2장 지급"
  ),

  RED(
      "Red",
      "30만원 이상, 70만원 미만",
      "월 15% 할인쿠폰 1장, 10% 할인쿠폰 1장 지급"
  ),

  ORANGE(
      "Orange",
      "10만원 이상, 30만원 미만",
      "월 10% 할인쿠폰 2장 지급"
  ),

  GREEN(
      "Green",
      "10만원 미만",
      "월 10% 할인쿠폰 1장, 신규 가입 시 앱 전용 15% 할인쿠폰 1장 지급 (가입 후 30일 간 유효)"
  );

  private final String levelName;       // 등급명
  private final String condition;       // 최근 6개월간 구매 금액 조건
  private final String benefit;         // 할인 혜택 설명

  LevelType(String levelName, String condition, String benefit) {
    this.levelName = levelName;
    this.condition = condition;
    this.benefit = benefit;
  }

}
