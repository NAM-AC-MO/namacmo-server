package com.namacmo.ledgerservice.v1.adater.out.persistence.repository

import com.namacmo.ledgerservice.v1.domain.DoubleLedgerEntry

interface LedgerEntryRepository {

  fun save(doubleLedgerEntries: List<DoubleLedgerEntry>)
}