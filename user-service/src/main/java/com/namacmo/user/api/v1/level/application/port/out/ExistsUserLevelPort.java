package com.namacmo.user.api.v1.level.application.port.out;

import com.namacmo.user.api.v1.level.domain.valueobject.UserId;
import java.util.UUID;

public interface ExistsUserLevelPort {
  boolean existsUserLevel(UserId userId);
}
