package com.namacmo.ledgerservice.v1.adater.out.persistence.repository

import com.namacmo.ledgerservice.v1.domain.DoubleAccountsForLedger
import com.namacmo.ledgerservice.v1.domain.FinanceType

interface AccountRepository {

  fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger
}