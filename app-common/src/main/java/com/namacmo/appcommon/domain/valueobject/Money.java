package com.namacmo.appcommon.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class Money {

  public static final Money ZERO = new Money(BigDecimal.ZERO);

  private final BigDecimal moneyAmount;

  public Money(String moneyAmount) {
    this.moneyAmount = validateMoneyAmount(moneyAmount);
  }

  private BigDecimal validateMoneyAmount(String moneyAmount) {
    try {
      final BigDecimal checkMoneyAmount = new BigDecimal(moneyAmount);
      if (checkMoneyAmount.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
      }
      return checkMoneyAmount;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("유효한 숫자 형식이 아닙니다.", e);
    }
  }

  public Money(BigDecimal moneyAmount) {
    if (moneyAmount == null) {
      throw new IllegalArgumentException("moneyAmount는 null일 수 없습니다.");
    }
    if (moneyAmount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
    }
    this.moneyAmount = moneyAmount;
  }

  public Money add(Money other) {
    return new Money(this.moneyAmount.add(other.moneyAmount));
  }

  public Money subtract(Money other) {
    return new Money(this.moneyAmount.subtract(other.moneyAmount));
  }

  public Money multiply(int factor) {
    return new Money(this.moneyAmount.multiply(BigDecimal.valueOf(factor)));
  }

  public Money divide(int divisor) {
    return new Money(this.moneyAmount.divide(BigDecimal.valueOf(divisor)));
  }

  public boolean isGreaterThan(Money other) {
    return this.moneyAmount.compareTo(other.moneyAmount) > 0;
  }

  public boolean isLessThan(Money other) {
    return this.moneyAmount.compareTo(other.moneyAmount) < 0;
  }

  public boolean isEqualTo(Money other) {
    return this.moneyAmount.compareTo(other.moneyAmount) == 0;
  }

  public String toStringValue() {
    return this.moneyAmount.toString();
  }

  public BigDecimal getValue() {
    return this.moneyAmount;
  }

  @Override
  public String toString() {
    return "Money{" +
        "moneyAmount=" + moneyAmount +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money money = (Money) o;
    return Objects.equals(moneyAmount, money.moneyAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(moneyAmount);
  }
}
