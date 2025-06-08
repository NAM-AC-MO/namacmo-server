package com.namacmo.paymentservice.v1.application.port.out

import com.namacmo.paymentservice.v1.domain.PaymentEventMessage

interface DispatchEventMessagePort {

  fun dispatch(paymentEventMessage: PaymentEventMessage)
}