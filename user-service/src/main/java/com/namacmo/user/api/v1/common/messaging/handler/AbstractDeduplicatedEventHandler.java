package com.namacmo.user.api.v1.common.messaging.handler;

import com.namacmo.user.api.v1.common.messaging.deduplication.application.port.in.MessageDeduplicationUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 중복 메시지 처리를 방지하는 추상 이벤트 핸들러 클래스입니다.
 * <p>
 * 이 클래스는 메시지 중복 여부를 확인하고, 중복이 아닐 경우에만
 * 실제 비즈니스 로직을 수행하도록 설계되었습니다.
 * </p>
 *
 * <p><strong>사용법:</strong> 하위 클래스는 {@link #process(Object)} 메서드를 구현하여
 * 비즈니스 로직을 작성해야 합니다. {@link #handle(Object, UUID)} 메서드는 재정의하지 마십시오.</p>
 *
 * @param <T> 처리할 메시지 타입
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDeduplicatedEventHandler<T> {

  private final MessageDeduplicationUseCase messageDeduplicationUseCase;

  /**
   * 메시지를 처리하는 메서드입니다.
   * <p>
   * 메시지 ID를 기준으로 중복 여부를 확인하고, 중복이 아닐 경우에만
   * {@link #process(Object)} 메서드를 호출하여 실제 처리를 수행합니다.
   * <br>
   * 이 메서드는 {@code @Transactional}이 적용되어 있으며, 트랜잭션 내에서 실행됩니다.
   * <br>
   * 주의: {@code final} 키워드를 붙일 수 없으므로, 이 메서드를 재정의하지 마시고
   * 대신 {@link #process(Object)} 만 구현하십시오.
   * </p>
   *
   * @param message   처리할 이벤트 객체
   * @param messageId 메시지 식별용 UUID
   * @throws RuntimeException 처리 중 예외 발생 시 트랜잭션 롤백을 유도하기 위해 다시 던집니다.
   */
  @Transactional
  public void handle(T message, UUID messageId) {
    if (messageDeduplicationUseCase.isProcessed(messageId)) {
      log.info("중복 메시지 무시됨. messageId: {}", messageId);
      return;
    }

    log.info("메시지 처리 시작. messageId: {}", messageId);
    try {
      process(message);
      messageDeduplicationUseCase.markProcessed(messageId);
    } catch (Exception e) {
      log.error("메시지 처리 중 예외 발생. messageId: {}", messageId, e);
      throw e;
    }
  }

  /**
   * 하위 클래스에서 구현해야 하는 실제 비즈니스 로직 처리 메서드입니다.
   *
   * @param message 처리할 이벤트 객체
   */
  protected abstract void process(T message);
}
