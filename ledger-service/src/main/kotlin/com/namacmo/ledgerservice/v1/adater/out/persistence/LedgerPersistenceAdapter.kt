package com.namacmo.ledgerservice.v1.adater.out.persistence

import com.namacmo.appcommon.hexagonal.PersistenceAdapter
import com.namacmo.ledgerservice.v1.adater.out.persistence.repository.LedgerEntryRepository
import com.namacmo.ledgerservice.v1.adater.out.persistence.repository.LedgerTransactionRepository
import com.namacmo.ledgerservice.v1.application.port.out.DuplicateMessageFilterPort
import com.namacmo.ledgerservice.v1.application.port.out.SaveDoubleLedgerEntryPort
import com.namacmo.ledgerservice.v1.domain.DoubleLedgerEntry
import com.namacmo.ledgerservice.v1.domain.PaymentEventMessage


@PersistenceAdapter
class LedgerPersistenceAdapter (
  private val ledgerTransactionRepository: LedgerTransactionRepository,
  private val ledgerEntryRepository: LedgerEntryRepository
) : DuplicateMessageFilterPort, SaveDoubleLedgerEntryPort {

  override fun isAlreadyProcess(message: PaymentEventMessage): Boolean {
    return ledgerTransactionRepository.isExist(message)
  }

  override fun save(doubleLedgerEntries: List<DoubleLedgerEntry>) {
    return ledgerEntryRepository.save(doubleLedgerEntries)
  }
}