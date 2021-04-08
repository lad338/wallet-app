package com.example.walletapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

  @Value("${jwt.secret}")
  private String JWT_SECRET;

  private static final int EXPIRY = 1800000;

  public String getJwtSecret() {
    return JWT_SECRET;
  }

  public int getExpiry() {
    return 1800000;
  }
}
