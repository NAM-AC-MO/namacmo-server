package com.namacmo.paymentservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(scanBasePackages = ["com.namacmo.infracommon.kafka", "com.namacmo.paymentservice.v1"])
class PaymentServiceApplication

fun main(args: Array<String>) {
    runApplication<PaymentServiceApplication>(*args)
}
