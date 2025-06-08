package com.namacmo.paymentservice.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

/**
 * Jackson ObjectMapper 설정
 *
 * - Java 객체 직렬화를 기본으로 동작하는 Jackson에 Kotlin 모듈을 등록하여,
 *   Kotlin의 data class, nullability 등을 정확히 처리할 수 있도록 합니다.
 * - JDK8 및 Java Time API 지원 모듈도 함께 등록합니다.
 * - 날짜와 시간을 직렬화할 때 타임스탬프 대신 사람이 읽을 수 있는 ISO-8601 형식으로 출력되도록 설정합니다.
 */
val objectMapper = ObjectMapper()
    .registerKotlinModule()
    .registerModules(Jdk8Module(), JavaTimeModule())
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

