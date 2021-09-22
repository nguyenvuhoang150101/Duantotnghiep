package com.example.security.config.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.example.security.config.SecurityConstants;

@Configuration
@Aspect
@ConditionalOnProperty(prefix = "app.debug", name = "trace-exception", havingValue = "true")
public class ExceptionTraceAspect {
  @AfterThrowing(pointcut = "execution(* " + SecurityConstants.PACKAGE_BASE + ".*(..))", throwing = "ex")
  public void logError(Exception ex) {
    ex.printStackTrace();
  }
}
