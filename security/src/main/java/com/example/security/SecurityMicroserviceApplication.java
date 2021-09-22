package com.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.example.security.config.SecurityConstants;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = { SecurityConstants.PACKAGE_BASE })
public class SecurityMicroserviceApplication {
  public static void main(String[] args) {
    SpringApplication.run(SecurityMicroserviceApplication.class, args);
  }
}
