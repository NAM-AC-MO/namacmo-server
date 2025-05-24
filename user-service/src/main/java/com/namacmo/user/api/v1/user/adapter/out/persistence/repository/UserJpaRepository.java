package com.namacmo.user.api.v1.user.adapter.out.persistence.repository;

import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {
  Optional<UserJpaEntity> findByEmail(String email);
}
