package com.namacmo.ledgerservice.v1.adater.out.persistence.entity

import com.namacmo.ledgerservice.v1.domain.Account
import org.springframework.stereotype.Component

@Component
class JpaAccountMapper {

  fun mapToDomainEntity(jpaAccountEntity: JpaAccountEntity): Account {
    return Account(
      id = jpaAccountEntity.id!!,
      name = jpaAccountEntity.name
    )
  }
}