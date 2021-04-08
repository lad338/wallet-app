package com.example.walletapp.model.service;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionSnapshot {

  private Date time;
  private BigDecimal exchangeRate;
  private BigDecimal exchangedAmount;
}
