package com.example.walletapp.model.service;

import com.example.walletapp.constant.Currency;
import com.example.walletapp.exception.UnexpectedException;
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

  public BigDecimal getAmountByCurrency(Currency currency) {
    switch (currency) {
      case HKD:
        return hkd;
      case USD:
        return usd;
      case EUR:
        return eur;
    }
    throw new UnexpectedException("Unknown currency " + currency);
  }
}
