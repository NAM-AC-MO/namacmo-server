package com.namacmo.user.api.v1.level.adapter.out.persistent;

import com.namacmo.user.api.v1.common.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigInteger;

@Converter
public class MoneyToBigIntegerConvert implements AttributeConverter<Money, BigInteger> {

  @Override
  public BigInteger convertToDatabaseColumn(Money money) {
    return money.getMoneyAmount();
  }

  @Override
  public Money convertToEntityAttribute(BigInteger bigInteger) {
    return new Money(bigInteger);
  }
}
