package com.namacmo.ledgerservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.namacmo.ledgerservice.v1.application.port.`in`.DoubleLedgerEntryRecordUseCase
import com.namacmo.ledgerservice.v1.application.port.out.DuplicateMessageFilterPort
import com.namacmo.ledgerservice.v1.application.port.out.LoadAccountPort
import com.namacmo.ledgerservice.v1.application.port.out.LoadPaymentOrderPort
import com.namacmo.ledgerservice.v1.application.port.out.SaveDoubleLedgerEntryPort
import com.namacmo.ledgerservice.v1.domain.FinanceType
import com.namacmo.ledgerservice.v1.domain.Ledger
import com.namacmo.ledgerservice.v1.domain.LedgerEventMessage
import com.namacmo.ledgerservice.v1.domain.LedgerEventMessageType
import com.namacmo.ledgerservice.v1.domain.PaymentEventMessage

@UseCase
class DoubleLedgerEntryRecordService (
  private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  private val loadAccountPort: LoadAccountPort,
  private val loadPaymentOrderPort: LoadPaymentOrderPort,
  private val saveDoubleLedgerEntryPort: SaveDoubleLedgerEntryPort
) : DoubleLedgerEntryRecordUseCase {

  override fun recordDoubleLedgerEntry(message: PaymentEventMessage): LedgerEventMessage {
    if (duplicateMessageFilterPort.isAlreadyProcess(message)) {
      return createLedgerEventMessage(message)
     }

    val doubleAccountsForLedger = loadAccountPort.getDoubleAccountsForLedger(FinanceType.PAYMENT_ORDER)
    val paymentOrders = loadPaymentOrderPort.getPaymentOrders(message.orderId())

    val doubleLedgerEntries = Ledger.createDoubleLedgerEntry(doubleAccountsForLedger, paymentOrders)

    saveDoubleLedgerEntryPort.save(doubleLedgerEntries)

    return createLedgerEventMessage(message)
  }

  private fun createLedgerEventMessage(message: PaymentEventMessage) =
    LedgerEventMessage(
      type = LedgerEventMessageType.SUCCESS,
      payload = mapOf(
        "orderId" to message.orderId()
      )
    )
}