package com.namacmo.user.api.v1.user.domain.valueobject;

import com.namacmo.appcommon.domain.valueobject.BaseId;
import java.util.UUID;
import lombok.Getter;

@Getter
public final class UserId extends BaseId<String> {

  public UserId() {
    super(generateUserId());
  }

  public UserId(String userId) {
    super(userId);
  }

  private static String generateUserId() {
    return UUID.randomUUID().toString();
  }
}
