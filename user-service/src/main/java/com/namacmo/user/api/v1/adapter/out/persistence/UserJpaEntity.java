package com.namacmo.user.api.v1.adapter.out.persistence;

import com.namacmo.user.api.v1.domain.Role;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
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

  // 양방향 매핑 추가
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserLevelJpaEntity> userLevels = new HashSet<>();
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<RewardPointHistoryJpaEntity> userRewardPoints = new HashSet<>();

  // 편의 메서드
  public void addUserLevel(UserLevelJpaEntity userLevel) {
    userLevels.add(userLevel);
    userLevel.setUserJpaEntity(this); // 연관관계 주인 쪽에도 설정
  }

  // 편의 메서드
  public void addUserRewardPoint(RewardPointHistoryJpaEntity userRewardPoint) {
    userRewardPoints.add(userRewardPoint);
    userRewardPoint.setUserJpaEntity(this); // 연관관계 주인 쪽에도 설정
  }

}
