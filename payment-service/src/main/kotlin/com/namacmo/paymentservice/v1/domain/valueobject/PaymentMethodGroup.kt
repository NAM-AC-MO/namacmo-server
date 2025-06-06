package com.namacmo.paymentservice.v1.domain.valueobject

enum class PaymentMethodGroup(val method: String, val paymentTypes: List<PaymentType>) {
  EASY_PAY("간편결제", listOf(PaymentType.TOSS, PaymentType.KAKAO_PAY)),
  CARD("카드결제", listOf(PaymentType.KB_CARD, PaymentType.SINHAN_CARD)),
  OTHER("기타결제", listOf(PaymentType.PAYCO, PaymentType.KB_PAY, PaymentType.NAVER_PAY));

  companion object {
    private val methodMap: Map<String, PaymentMethodGroup> = entries.associateBy { it.method }

    private val typeToGroupMap: Map<PaymentType, PaymentMethodGroup> = entries
      .flatMap { group -> group.paymentTypes.map { it to group } }
      .toMap()

    fun findByMethod(method: String): PaymentMethodGroup = methodMap[method] ?: error("method '$method'은 올바르지 않은 결제 방법입니다.")
    fun findByMethodOrNull(method: String): PaymentMethodGroup? = methodMap[method]

    fun findByPayType(paymentType: PaymentType): PaymentMethodGroup =
      typeToGroupMap[paymentType]
        ?: error("PaymentType '$paymentType'은 올바르지 않은 결제 방법입니다.")

    fun findByPayTypeOrNull(paymentType: PaymentType): PaymentMethodGroup? = typeToGroupMap[paymentType]
  }

  fun hasPayCode(payType: PaymentType): Boolean = payType in paymentTypes
}
