package com.namacmo.infracommon.kafka.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public final class PartitionKeyUtil {

  @Value("${kafka-config.num-of-partitions}")
  private Integer numberOfPartitions;

  @Value("${kafka-config.partition-policy:default}")
  private String partitionHashAlgorithmPolicy;

  private PartitionHashPolicy partitionPolicy;

  @PostConstruct
  public void validateConfig() {
    if (numberOfPartitions == null || numberOfPartitions <= 0) {
      throw new IllegalArgumentException("[KafkaConfig] num-of-partitions 설정은 1 이상이어야 합니다.");
    }

    partitionPolicy = PartitionHashPolicy.fromName(partitionHashAlgorithmPolicy);
    log.info("[KafkaConfig] 파티션 설정 정상 적용됨. partitions={}, policy={}", numberOfPartitions, partitionPolicy.getPolicyName());
  }

  public int createPartitionKey(String key) {
    if (key == null) {
      throw new IllegalArgumentException("파티션 키는 null일 수 없습니다.");
    }

    int hashValue = partitionPolicy.hash(key);
    return Math.abs(hashValue) % numberOfPartitions;
  }

}
