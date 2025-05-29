package com.namacmo.user.api.v1.level.adapter.out.persistence.repository;

import com.namacmo.user.api.v1.level.adapter.out.persistence.entity.UserLevelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLevelJpaRepository extends JpaRepository<UserLevelJpaEntity, String> {
  boolean existsByUserId(String userId);
}
