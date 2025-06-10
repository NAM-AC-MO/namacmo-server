package com.namacmo.user.api.v1.point.adapter.out.persistent.repository;

import com.namacmo.user.api.v1.point.adapter.out.persistent.entity.RewardPointHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardPointHistoryJpaRepository extends JpaRepository<RewardPointHistoryJpaEntity, String> {
}
