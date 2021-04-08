package com.example.walletapp.model.service;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistration implements LoginAdapter {

  private String username;
  private String password;
  private BigDecimal hkd;
  private BigDecimal usd;
  private BigDecimal eur;
}
