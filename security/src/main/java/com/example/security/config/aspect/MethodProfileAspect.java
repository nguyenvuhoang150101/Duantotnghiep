package com.example.security.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.example.security.config.SecurityConstants;

@Aspect
@Configuration
@ConditionalOnProperty(prefix = "app.debug", name = "profile-method", havingValue = "true")
public class MethodProfileAspect {
  @Pointcut("execution(* " + SecurityConstants.PACKAGE_BASE + ".*(..))")
  public void profileMethods() {
  }

  @Around("profileMethods()")
  public Object profile(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    System.out.println("Going to call the method.");
    Object output = pjp.proceed();
    System.out.println("Method execution completed.");
    long elapsedTime = System.currentTimeMillis() - start;
    System.out.println("Method execution time: " + elapsedTime + " milliseconds.");
    return output;
  }
}
