package com.example.security.dto.user;

import com.example.common.dto.response.SuccessResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUserResponse  extends SuccessResponse{
  private String id;
  private String userName;
  private String displayName;
  private String email;
}
