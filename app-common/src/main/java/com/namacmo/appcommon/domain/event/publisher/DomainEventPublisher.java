package com.namacmo.appcommon.domain.event.publisher;

import com.namacmo.appcommon.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
