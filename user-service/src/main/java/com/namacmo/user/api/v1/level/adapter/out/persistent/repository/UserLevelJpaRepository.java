package com.namacmo.user.api.v1.level.adapter.out.persistent.repository;

import com.namacmo.user.api.v1.level.adapter.out.persistent.entity.UserLevelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLevelJpaRepository extends JpaRepository<UserLevelJpaEntity, String> {
  boolean existsByUserId(String userId);
}
