package com.namacmo.user.api.v1.user.adapter.out.persistence.entity;

import com.namacmo.user.api.v1.user.domain.model.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  @Embedded
  private AddressJpaVo address;
  @Column(name = "email")
  private String email;
  @Column(name = "name")
  private String name;
  @Column(name = "phone")
  private String phone;
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "roles",
      joinColumns = @JoinColumn(name = "user_id")
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "name")
  private Set<Role> roles = new HashSet<>();

  @Builder
  private UserJpaEntity(
      AddressJpaVo address,
      String email,
      String name,
      String phone
  ) {
    this.address = address;
    this.email = email;
    this.name = name;
    this.phone = phone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserJpaEntity that = (UserJpaEntity) o;
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }
}
