package com.namacmo.user.api.v1.user.adapter.in.web.request;

public record RegisterUserRequest(
    String streetAddress,
    String detailAddress,
    String city,
    String zipCode,
    String email,
    String name,
    String phone
) {
}
