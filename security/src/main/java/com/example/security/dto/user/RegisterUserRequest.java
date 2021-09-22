package com.example.security.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.common.config.Constants;
import com.example.common.validator.FieldMatch;
import com.example.security.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword")
public class RegisterUserRequest {
  @NotNull
  @Size(min = User.NAME_MIN_LENGTH, max = User.NAME_MAX_LENGTH)
  private String name;
  
  @NotNull
  @Size(min = User.DISPLAY_NAME_MIN_LENGTH, max = User.DISPLAY_NAME_MAX_LENGTH)
  private String displayName;
  
  @NotNull
  @Pattern(regexp = Constants.PATTERN_PASSWORD)
  private String password;
  
  @NotNull
  @Pattern(regexp = Constants.PATTERN_PASSWORD)
  private String confirmPassword;
  
  @Email
  private String email;
}
