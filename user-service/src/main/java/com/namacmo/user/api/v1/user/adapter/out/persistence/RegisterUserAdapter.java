package com.namacmo.user.api.v1.user.adapter.out.persistence;

import com.namacmo.appcommon.PersistenceAdapter;
import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.AddressJpaVo;
import com.namacmo.user.api.v1.user.adapter.out.persistence.entity.UserJpaEntity;
import com.namacmo.user.api.v1.user.adapter.out.persistence.repository.UserJpaRepository;
import com.namacmo.user.api.v1.user.application.port.out.RegisterUserPort;
import com.namacmo.user.api.v1.user.domain.model.Address;
import com.namacmo.user.api.v1.user.domain.model.Role;
import com.namacmo.user.api.v1.user.domain.model.UserProfile;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegisterUserAdapter implements RegisterUserPort {

  private final UserJpaRepository userJpaRepository;

  @Override
  public UserJpaEntity registerUser(UserProfile userProfile) {
    final Address address = userProfile.getAddress();
    final AddressJpaVo addressJpaVo = AddressJpaVo.builder()
        .streetAddress(address.getStreetAddress())
        .detailAddress(address.getDetailAddress())
        .city(address.getCity())
        .zipCode(address.getZipCode())
        .build();

    final UserJpaEntity userJpaEntity = UserJpaEntity.builder()
        .address(addressJpaVo)
        .email(userProfile.getEmail())
        .name(userProfile.getName())
        .phone(userProfile.getPhone())
        .build();
    userJpaEntity.addRole(Role.USER);
    userJpaEntity.publishUserRegisteredEvent();
    return userJpaRepository.save(userJpaEntity);
  }
}
