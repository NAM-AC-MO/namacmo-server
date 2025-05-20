package com.namacmo.configclient.adapter.in.web;

import com.namacmo.appcommon.WebAdapter;
import com.namacmo.configclient.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

  private final ApplicationConfig applicationConfig;

  @GetMapping("/config")
  public ResponseEntity<String> config() {
    log.info("app-config={}", applicationConfig);
    return ResponseEntity.ok(applicationConfig.toString());
  }

}