package com.namacmo.user.api.v1.user.adapter.out.persistent.entity;

import com.namacmo.user.api.v1.user.adapter.out.persistent.valueobject.AddressJpaVo;
import com.namacmo.user.api.v1.user.domain.valueobject.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity {
  @Id
  private String userId;
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
  public UserJpaEntity(
      String userId,
      AddressJpaVo address,
      String email,
      String name,
      String phone,
      Set<Role> roles
  ) {
    this.userId = userId;
    this.address = address;
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.roles = roles;
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  public Set<Role> getRoles() {
    return Collections.unmodifiableSet(roles);
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
