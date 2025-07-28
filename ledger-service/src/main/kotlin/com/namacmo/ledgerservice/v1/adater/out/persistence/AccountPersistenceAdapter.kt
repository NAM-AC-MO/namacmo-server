package com.namacmo.ledgerservice.v1.adater.out.persistence

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.namacmo.ledgerservice.v1.adater.out.persistence.repository.AccountRepository
import com.namacmo.ledgerservice.v1.application.port.out.LoadAccountPort
import com.namacmo.ledgerservice.v1.domain.DoubleAccountsForLedger
import com.namacmo.ledgerservice.v1.domain.FinanceType


@PersistenceAdapter
class AccountPersistenceAdapter (
  private val accountRepository: AccountRepository
) : LoadAccountPort {

  override fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger {
    return accountRepository.getDoubleAccountsForLedger(financeType)
  }
}