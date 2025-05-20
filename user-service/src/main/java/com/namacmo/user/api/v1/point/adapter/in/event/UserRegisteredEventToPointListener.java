package com.namacmo.user.api.v1.point.adapter.in.event;

import com.namacmo.user.api.v1.user.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegisteredEventToPointListener {

  @EventListener
  public void handler(UserRegisteredEvent event) {
    log.info("subscribe event={}", event);
  }
}
