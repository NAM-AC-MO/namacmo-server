package com.namacmo.user.api.v1.user.adapter.in.web;

import com.namacmo.appcommon.WebAdapter;
import com.namacmo.user.api.v1.user.adapter.in.web.request.RegisterUserRequest;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserCommand;
import com.namacmo.user.api.v1.user.application.port.in.RegisterUserUseCase;
import com.namacmo.user.api.v1.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegisterUserController {

  private final RegisterUserUseCase registerUserUseCase;

  @PostMapping("/users")
  public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequest request) {
    final RegisterUserCommand command = RegisterUserCommand.builder()
        .streetAddress(request.streetAddress())
        .detailAddress(request.detailAddress())
        .city(request.city())
        .zipCode(request.zipCode())
        .email(request.email())
        .name(request.name())
        .phone(request.phone())
        .build();

    return ResponseEntity.ok(registerUserUseCase.registerUser(command));
  }
}
