package com.namacmo.user.api.v1.user.adapter.out.persistence.mapper;

import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.AddressJpaVo;
import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.domain.model.Address;
import com.namacmo.user.api.v1.user.domain.model.Roles;
import com.namacmo.user.api.v1.user.domain.model.User;
import com.namacmo.user.api.v1.user.domain.model.UserNo;
import com.namacmo.user.api.v1.user.domain.model.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserMapper {
  public User mapToDomain(UserJpaEntity userEntity) {
    final AddressJpaVo addressVo = userEntity.getAddress();
    final Address address = Address.builder()
        .streetAddress(addressVo.getStreetAddress())
        .detailAddress(addressVo.getDetailAddress())
        .city(addressVo.getCity())
        .zipCode(addressVo.getZipCode())
        .build();
    final UserProfile userProfile = UserProfile.builder()
        .address(address)
        .email(userEntity.getEmail())
        .name(userEntity.getName())
        .phone(userEntity.getPhone())
        .build();

    return User.builder()
        .userNo(new UserNo(String.valueOf(userEntity.getUserId())))
        .userProfile(userProfile)
        .roles(new Roles(userEntity.getRoles()))
        .build();
  }
}
