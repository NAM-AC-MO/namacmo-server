package com.payment.configclient.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@ConfigurationProperties("com.htwo")
@RefreshScope
@ToString
public class ApplicationConfig {
  private String profile;
  private String region;
}
