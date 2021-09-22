package com.example.security.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
  @NotNull
  private String name;
  @NotNull
  @Email
  private String email;
}
