package com.namacmo.paymentservice.v1.domain.valueobject

enum class PaymentType(description: String) {
  TOSS("토스"),
  KAKAO_PAY("카카오페이"),
  KB_CARD("국민은행"),
  SINHAN_CARD("신한은행"),
  PAYCO("페이코"),
  KB_PAY("국민은행페이"),
  NAVER_PAY("네이버페이");
  companion object {
    private val NAME_MAP = entries.associateBy { it.name }

    fun findByName(name: String): PaymentType =
      NAME_MAP[name] ?: error("PaymentType '$name'은 올바르지 않은 결제 방법입니다.")

    fun findByNameOrNull(name: String): PaymentType? = NAME_MAP[name]
  }
}