package com.namacmo.user.api.v1.user.domain.model.factory;

import com.namacmo.user.api.v1.user.domain.valueobject.Role;
import com.namacmo.user.api.v1.user.domain.valueobject.Roles;
import java.util.HashSet;
import java.util.Set;

public final class RolesFactory {
  private RolesFactory() {}

  public static Roles of(Role... roles) {
    Set<Role> roleSet = new HashSet<>();
    if (roles == null || roles.length == 0) {
      roleSet.add(Role.USER);
    } else {
      roleSet.addAll(Set.of(roles));
    }
    return new Roles(roleSet);
  }
}

