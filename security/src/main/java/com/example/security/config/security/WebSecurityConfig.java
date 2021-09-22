package com.example.security.config.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.common.util.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private AuthenticationSuccessHandler loginSuccessHandler;
  @Autowired
  private AuthenticationFailureHandler loginFailureHandler;
  @Autowired
  private LogoutSuccessHandler logoutSuccessHandler;
  @Autowired
  private JwtAuthFilter jwtAuthenticationFilter;
  @Autowired
  private UserDetailsService userDetailService;
  @Value("${app.api.allowed-origins}")
  private String[] allowedOrigin;
  
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    httpSecurity.cors().configurationSource(new CorsConfigSource());
    httpSecurity.sessionManagement().sessionFixation().newSession().maximumSessions(1).maxSessionsPreventsLogin(true);
    httpSecurity.authorizeRequests().anyRequest().permitAll();
    httpSecurity.authorizeRequests().and().formLogin().loginProcessingUrl("/api/v1/login")
        .usernameParameter("username").passwordParameter("password").successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler);
    httpSecurity.authorizeRequests().and().logout().logoutUrl("/api/v1/logout")
        .logoutSuccessHandler(logoutSuccessHandler);
    httpSecurity.authorizeRequests().and().addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);
    httpSecurity.userDetailsService(userDetailService);
  }

  class CorsConfigSource implements CorsConfigurationSource {
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
      CorsConfiguration cors = new CorsConfiguration();
      String[] methodArray = { "*" };
      String[] headerArray = { "*" };
      cors.setAllowedOrigins(Arrays.asList(allowedOrigin));
      cors.setAllowedMethods(Arrays.asList(methodArray));
      cors.setAllowedHeaders(Arrays.asList(headerArray));
      return cors;
    }
  }
}
