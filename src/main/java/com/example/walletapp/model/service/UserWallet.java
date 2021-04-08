package com.example.walletapp.model.service;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWallet {

  private BigDecimal hkd;
  private BigDecimal usd;
  private BigDecimal eur;
}
