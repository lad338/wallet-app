package com.example.walletapp.model.api.response;

import com.example.walletapp.constant.TransactionAction;
import com.example.walletapp.constant.TransactionRecordAction;
import com.example.walletapp.model.repository.Transaction;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionHistoryResponse {

  private List<TransactionRecord> transactionRecords;

  @Data
  @Builder
  public static class TransactionRecord {

    private String id;
    private String fromId;
    private String targetId;
    private String amount;
    private String targetAmount;
    private String currency;
    private String targetCurrency;
    private TransactionRecordAction transactionAction;
    private String exchangeRate;
    private Date executedAt;

    public static TransactionRecord fromTransaction(Transaction transaction, String userId) {
      final TransactionRecordBuilder builder = TransactionRecord
        .builder()
        .id(transaction.getId().toHexString())
        .executedAt(transaction.getExecutedAt())
        .amount(transaction.getAmount())
        .currency(transaction.getCurrency());

      if (TransactionAction.TRANSFER.toString().equals(transaction.getTransactionAction())) {
        if (userId.equals(transaction.getUserId())) {
          builder.targetId(transaction.getTargetId());
          builder.transactionAction(TransactionRecordAction.TRANSFER);
        } else if (userId.equals(transaction.getTargetId())) {
          builder.fromId(transaction.getUserId());
          builder.transactionAction(TransactionRecordAction.RECEIVE);
        }
      } else {
        if (TransactionAction.EXCHANGE.toString().equals(transaction.getTransactionAction())) {
          builder
            .targetCurrency(transaction.getTargetCurrency())
            .exchangeRate(transaction.getExchangeRate())
            .targetAmount(transaction.getTargetAmount());
        }
        builder.transactionAction(
          TransactionRecordAction.valueOf(transaction.getTransactionAction())
        );
      }
      return builder.build();
    }
  }
}
