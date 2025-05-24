package com.payment.appcommon.domain.event;

/**
 * 도메인에서 해당 이벤트를 발생시키는 엔티티의 타입을 지정하기 위해 이벤트 객체에 마킹하는 클래스입니다.
 * 예를 들어, OrderCreatedEvent는 제네릭 타입으로 Order를 설정하는데, 이는 이 이벤트가 Order 엔티티에서 발생했음을 의미합니다.
 */
public interface DomainEvent<T> {
}
