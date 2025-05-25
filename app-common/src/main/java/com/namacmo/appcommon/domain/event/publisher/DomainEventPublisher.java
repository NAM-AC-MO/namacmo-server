package com.namacmo.appcommon.domain.event.publisher;

import com.namacmo.appcommon.domain.event.DomainEvent;
import java.util.List;

public interface DomainEventPublisher<T> {
    void publish(List<DomainEvent<T>> domainEvents);
}
