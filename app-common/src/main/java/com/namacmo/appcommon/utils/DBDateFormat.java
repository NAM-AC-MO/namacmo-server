package com.namacmo.appcommon.utils;

import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public enum DBDateFormat {
  MYSQL("yyyy-MM-dd HH:mm:ss"),
  POSTGRES("yyyy-MM-dd HH:mm:ss.SSS"),
  ORACLE("yyyy-MM-dd'T'HH:mm:ss"); // 예시

  private final DateTimeFormatter formatter;

  DBDateFormat(String pattern) {
    this.formatter = DateTimeFormatter.ofPattern(pattern);
  }

}
