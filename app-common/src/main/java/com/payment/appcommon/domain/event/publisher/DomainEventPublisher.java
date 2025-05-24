package com.payment.appcommon.domain.event.publisher;

import com.payment.appcommon.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
