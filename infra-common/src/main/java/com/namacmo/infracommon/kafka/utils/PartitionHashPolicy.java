package com.namacmo.infracommon.kafka.utils;

import java.nio.charset.StandardCharsets;
import java.util.function.ToIntFunction;
import lombok.Getter;
import org.apache.commons.lang3.stream.Streams;
import org.apache.kafka.common.utils.Utils;

public enum PartitionHashPolicy {
  MURMUR2("murmur2", key -> Utils.murmur2(key.getBytes(StandardCharsets.UTF_8))),
  DEFAULT("default", String::hashCode);

  @Getter
  private final String policyName;
  private final ToIntFunction<String> hashFunction;

  PartitionHashPolicy(String policyName, ToIntFunction<String> hashFunction) {
    this.policyName = policyName;
    this.hashFunction = hashFunction;
  }

  public int hash(String key) {
    return hashFunction.applyAsInt(key);
  }

  public static PartitionHashPolicy fromName(String policyName) {
    return Streams.of(values())
        .filter(policy -> policy.policyName.equalsIgnoreCase(policyName))
        .findFirst()
        .orElse(DEFAULT);
  }
}
