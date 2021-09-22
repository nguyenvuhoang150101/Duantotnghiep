package com.example.security.dto.user;

import java.time.LocalDateTime;

import com.example.common.dto.response.SuccessResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthSuccessResponse extends SuccessResponse {
  private String accessToken;
  private String refreshToken;
  private LocalDateTime expiresIn;
//  private String id;
//  private String username;
//  private String fullname;
//  private String email;
//  private String pic;
//  private String[] roles;
//  private String language;
//  private String companyName;
//  private String phone;
}
