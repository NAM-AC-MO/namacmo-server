package com.namacmo.paymentservice.v1.application.service

import com.namacmo.appcommon.hexagonal.UseCase
import com.namacmo.paymentservice.common.Logger
import com.namacmo.paymentservice.v1.application.port.`in`.PaymentEventMessageRelayUseCase
import com.namacmo.paymentservice.v1.application.port.out.DispatchEventMessagePort
import com.namacmo.paymentservice.v1.application.port.out.LoadPendingPaymentEventMessagePort
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

@UseCase
class PaymentEventMessageRelayService (
  private val loadPendingPaymentEventMessagePort: LoadPendingPaymentEventMessagePort,
  private val dispatchEventMessagePort: DispatchEventMessagePort
) : PaymentEventMessageRelayUseCase {

  private val scheduler = Schedulers.newSingle("message-relay")

  @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
  override fun relay() {
    loadPendingPaymentEventMessagePort.getPendingPaymentEventMessage()
      .map { dispatchEventMessagePort.dispatch(it) }
      .onErrorContinue { err, _ ->  Logger.error("messageRelay", err.message ?: "failed to relay message.", err)}
      .subscribeOn(scheduler)
      .subscribe()
  }
}