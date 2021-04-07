package com.example.walletapp.model.service;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserRegistration {
  private String username;
  private String password;
  private BigDecimal hkd;
  private BigDecimal usd;
  private BigDecimal eur;

}
