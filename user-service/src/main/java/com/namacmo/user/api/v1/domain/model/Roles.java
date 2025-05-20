package com.namacmo.user.api.v1.domain.model;

import com.namacmo.user.api.v1.domain.Role;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Roles {
  private final Set<Role> roles = new HashSet<>();

  public Roles(Set<Role> roles) {
    this.roles.addAll(roles);
  }

  public boolean hasRole(Role role) {
    return roles.contains(role);
  }

  public Set<Role> getRoles() {
    return Collections.unmodifiableSet(roles);
  }
}
