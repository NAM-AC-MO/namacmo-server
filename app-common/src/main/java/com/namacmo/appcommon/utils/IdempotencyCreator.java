package com.namacmo.appcommon.utils;

import java.util.UUID;

public final class IdempotencyCreator {
  private IdempotencyCreator() {
    throw new IllegalStateException();
  }

  public static String create(Object data) {
    return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
  }
}
