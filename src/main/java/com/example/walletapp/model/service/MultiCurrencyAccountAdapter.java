package com.example.walletapp.model.service;

public interface MultiCurrencyAccountAdapter<Currency> {
  String getAmountByCurrency(Currency currency);
  void setAmountByCurrency(Currency currency, String amount);
}
