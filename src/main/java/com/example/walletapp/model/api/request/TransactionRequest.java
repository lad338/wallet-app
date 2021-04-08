package com.example.walletapp.model.api.request;

import com.example.walletapp.constant.Currency;
import com.example.walletapp.constant.TransactionAction;
import lombok.Data;

@Data
public class TransactionRequest {

  private String amount;

  private Currency currency;

  private TransactionAction action;

  //nullable for exchange
  private String targetUsername;

  //nullable for non exchange
  private Currency targetCurrency;
}
