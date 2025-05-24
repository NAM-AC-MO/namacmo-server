package com.namacmo.user.api.v1.user.application.exception;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String email) {
    super("이미 존재하는 사용자입니다: " + email);
  }
}

