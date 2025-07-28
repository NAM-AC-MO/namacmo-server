package com.namacmo.ledgerservice.v1.application.port.out

import com.namacmo.ledgerservice.v1.domain.DoubleLedgerEntry

interface SaveDoubleLedgerEntryPort {

  fun save(doubleLedgerEntries: List<DoubleLedgerEntry>)
}