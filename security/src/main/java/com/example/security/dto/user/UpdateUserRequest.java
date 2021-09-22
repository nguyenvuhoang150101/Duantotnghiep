package com.example.security.dto.user;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.common.config.Constants;
import com.example.security.entity.User;
import com.example.security.enums.UserTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
  @NotNull
  @Size(max = User.NAME_MAX_LENGTH, min = User.NAME_MIN_LENGTH)
  private String name;

  @NotNull
  @Size(max = User.DISPLAY_NAME_MAX_LENGTH, min = User.DISPLAY_NAME_MIN_LENGTH)
  private String displayName;
  
  @NotNull
  private boolean isActive = true;

  @Min(User.FAIL_LOGIN_COUNT_MIN)
  @Max(User.FAIL_LOGIN_COUNT_MAX)
  private Integer failLoginCount;

  private Long testAmount;

  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  private String testSelectId;

  @Enumerated(EnumType.ORDINAL)
  private UserTypeEnum userType;
}
