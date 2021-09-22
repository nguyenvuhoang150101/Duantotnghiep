package com.example.security.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.common.config.Constants;
import com.example.common.validator.FieldMatch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmNewPassword")
public class UpdatePasswordRequest {
  @NotNull
  @Pattern(regexp = Constants.PATTERN_PASSWORD)
  private String currentPassword;
  
  @NotNull
  @Pattern(regexp = Constants.PATTERN_PASSWORD)
  private String newPassword;
  
  @NotNull
  @Pattern(regexp = Constants.PATTERN_PASSWORD)
  private String confirmNewPassword;
}
