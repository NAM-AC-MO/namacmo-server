package com.namacmo.appcommon.domain.entity;

import com.namacmo.appcommon.domain.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<T, ID> extends BaseEntity<ID> {

  private final List<DomainEvent<T>> domainEvents = new ArrayList<>();

  protected AggregateRoot(ID id) {
    super(id);
  }

  protected void registerEvent(DomainEvent<T> event) {
    this.domainEvents.add(event);
  }

  /**
   * 단순 조회에서 상태변경이 일어나는 경우 사이드 이펙트가 발생할 수 있습니다. getDomainEvents 내부에서 clearPendingEvents 호출을 하지 않고 명시적으로 호출합니다.
   */
  public List<DomainEvent<T>> getDomainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void clearDomainEvents() {
    this.domainEvents.clear();
  }
}
