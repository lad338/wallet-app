package com.example.walletapp.model.service;

import com.example.walletapp.constant.Currency;
import com.example.walletapp.constant.TransactionAction;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutableTransaction {

  private String userId;

  private String targetId;

  private BigDecimal amount;

  private Currency currency;

  private Currency targetCurrency;

  private TransactionAction action;
}
