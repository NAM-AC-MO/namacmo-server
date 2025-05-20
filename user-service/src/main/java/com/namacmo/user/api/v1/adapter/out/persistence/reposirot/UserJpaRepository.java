package com.namacmo.user.api.v1.adapter.out.persistence.reposirot;

import com.namacmo.user.api.v1.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
}
