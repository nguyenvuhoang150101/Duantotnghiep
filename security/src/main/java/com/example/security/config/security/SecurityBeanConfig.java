package com.example.security.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.example.common.config.Constants;
import com.example.common.model.SecurityConfigParam;
import com.example.common.service.JwtService;
import com.example.common.service.JwtServiceImpl;
import com.example.common.service.TokenService;
import com.example.common.util.JwtAuthFilter;
import com.example.security.config.handler.SigninFailureHandler;
import com.example.security.config.handler.SigninSuccessHandler;
import com.example.security.config.handler.SignoutSuccessHandler;
import com.example.security.service.CustomUserDetailsService;
import com.example.security.service.TokenServiceImpl;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityBeanConfig {
  @Bean
  @ConfigurationProperties(prefix = "app.security.jwt")
  SecurityConfigParam applicationConfig() {
    return new SecurityConfigParam();
  }

  @Bean
  TokenService tokenService() {
    return new TokenServiceImpl();
  }

  @Bean
  @Autowired
  JwtService jwtService(SecurityConfigParam securityParam, TokenService tokenService) {
    return new JwtServiceImpl(securityParam, tokenService);
  }

  @Bean
  @Autowired
  JwtAuthFilter jwtAuthFilter(JwtService jwtService) {
    return new JwtAuthFilter(jwtService);
  }

  @Bean
  AuthenticationFailureHandler signinFailureHandler() {
    return new SigninFailureHandler();
  }

  @Bean
  AuthenticationSuccessHandler signinSuccessHandler() {
    return new SigninSuccessHandler();
  }

  @Bean
  LogoutSuccessHandler signoutSuccessHandler() {
    return new SignoutSuccessHandler();
  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(11);
  }

  @Bean
  UserDetailsService userDetailService() {
    return new CustomUserDetailsService();
  }

  @Bean
  AuditorAware<String> auditorProvider() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return () -> Optional.ofNullable(authentication.getName());
    } else {
      return () -> Optional.ofNullable(Constants.USER_SYSTEM);
    }
  }
}
