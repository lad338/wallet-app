package com.example.walletapp.constant;

public enum TransactionRecordAction {
  DEPOSIT,
  WITHDRAW,
  TRANSFER,
  EXCHANGE,
  RECEIVE;

  public TransactionRecordAction fromAction(TransactionAction transactionAction) {
    switch (transactionAction) {
      case DEPOSIT:
        return DEPOSIT;
      case WITHDRAW:
        return WITHDRAW;
      case EXCHANGE:
        return EXCHANGE;
      default:
        return null;
    }
  }
}
