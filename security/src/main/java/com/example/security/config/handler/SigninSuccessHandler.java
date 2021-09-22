package com.example.security.config.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.common.model.CustomUserDetails;
import com.example.common.model.SecurityConfigParam;
import com.example.common.service.JwtService;
import com.example.security.dto.user.AuthSuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SigninSuccessHandler implements AuthenticationSuccessHandler {
  private ObjectMapper om = new ObjectMapper();;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private SecurityConfigParam securityParam;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    CustomUserDetails authUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    LocalDateTime issueDate = LocalDateTime.now();
    LocalDateTime aExpireDate = issueDate;
    LocalDateTime rExpireDate = issueDate;
    aExpireDate.plusSeconds(securityParam.getAccessTokenExpirationSecond());
    rExpireDate.plusSeconds(securityParam.getRefreshTokenExpirationSecond());
    String authToken = jwtService.generateAccessToken(authUser, issueDate, aExpireDate);
    String refreshToken = jwtService.generateRefreshToken(authUser, issueDate, rExpireDate);

    AuthSuccessResponse ret = new AuthSuccessResponse();
    ret.setAccessToken(authToken);
    ret.setRefreshToken(refreshToken);
    ret.setExpiresIn(aExpireDate);
    
    response.setStatus(HttpServletResponse.SC_OK);
    PrintWriter writer = response.getWriter();
    writer.write(om.writeValueAsString(ret));
  }
}