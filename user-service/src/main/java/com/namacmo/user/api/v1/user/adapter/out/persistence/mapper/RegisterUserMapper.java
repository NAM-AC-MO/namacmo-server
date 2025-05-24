package com.namacmo.user.api.v1.user.adapter.out.persistence.mapper;

import com.namacmo.user.api.v1.user.adapter.out.persistence.valueobject.AddressJpaVo;
import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.domain.valueobject.Address;
import com.namacmo.user.api.v1.user.domain.valueobject.Roles;
import com.namacmo.user.api.v1.user.domain.model.User;
import com.namacmo.user.api.v1.user.domain.valueobject.UserId;
import com.namacmo.user.api.v1.user.domain.valueobject.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserMapper {
  public UserJpaEntity mapToJpaEntity(User user) {
    final UserProfile userProfile = user.getUserProfile();
    final Address address = userProfile.getAddress();

    final AddressJpaVo addressJpaVo = AddressJpaVo.builder()
        .streetAddress(address.getStreetAddress())
        .detailAddress(address.getDetailAddress())
        .city(address.getCity())
        .zipCode(address.getZipCode())
        .build();

    return UserJpaEntity.builder()
        .userId(user.getUserId().getValue())
        .address(addressJpaVo)
        .email(userProfile.getEmail())
        .name(userProfile.getName())
        .phone(userProfile.getPhone())
        .roles(user.getRoles())
        .build();
  }

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
        .userId(new UserId(userEntity.getUserId()))
        .userProfile(userProfile)
        .roles(new Roles(userEntity.getRoles()))
        .build();
  }
}
