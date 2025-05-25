package com.namacmo.user.api.v1.level.domain.valueobject;

import com.namacmo.appcommon.domain.valueobject.BaseId;
import java.util.UUID;

public class UserLevelId extends BaseId<String> {

  public UserLevelId() {
    super(generateUserId());
  }

  protected UserLevelId(String value) {
    super(value);
  }

  private static String generateUserId() {
    return UUID.randomUUID().toString();
  }
}
