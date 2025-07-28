package com.namacmo.ledgerservice.v1.adater.out.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "accounts")
class JpaAccountEntity (

  @Id
  val id: Long? = null,

  val name: String
)