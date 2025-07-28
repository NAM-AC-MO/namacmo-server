package com.namacmo.ledgerservice.v1.application.port.out

import com.namacmo.ledgerservice.v1.domain.DoubleAccountsForLedger
import com.namacmo.ledgerservice.v1.domain.FinanceType


interface LoadAccountPort {

  fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger
}