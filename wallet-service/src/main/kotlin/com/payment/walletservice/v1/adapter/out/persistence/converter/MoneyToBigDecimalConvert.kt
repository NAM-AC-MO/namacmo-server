package com.payment.walletservice.v1.adapter.out.persistence.converter

import com.namacmo.appcommon.domain.valueobject.Money
import jakarta.persistence.AttributeConverter
import java.math.BigDecimal

class MoneyToBigDecimalConvert (): AttributeConverter<Money, BigDecimal> {

    override fun convertToDatabaseColumn(money: Money): BigDecimal {
        return money.moneyAmount
    }

    override fun convertToEntityAttribute(bigDecimal: BigDecimal): Money {
        return Money(bigDecimal)
    }
}