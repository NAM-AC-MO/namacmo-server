package com.namacmo.user.api.v1.common.messaging.deduplication.exception;

import java.util.UUID;

/**
 * 이미 처리된 메시지를 다시 처리하려 할 때 발생하는 예외입니다.
 * 이 예외는 중복 메시지 처리를 방지하기 위한 트랜잭션 롤백 용도로 사용됩니다.
 */
public class DuplicateMessageException extends RuntimeException {

  public DuplicateMessageException() {
    super("메시지가 이미 처리되었습니다.");
  }

  public DuplicateMessageException(UUID messageId) {
    super(messageId + "는 이미 처리된 메시지 입니다.");
  }
}
