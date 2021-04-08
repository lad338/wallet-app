package com.example.walletapp.model.repository;

import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Transaction {

  @Id
  private ObjectId id;

  @Indexed
  private String userId;

  @Indexed
  private String targetId;

  private String amount;

  private String targetAmount;

  private String currency;

  private String targetCurrency;

  private String transactionAction;

  private String exchangeRate;

  private Date executedAt;

  @CreatedDate
  private Date createdAt;
}
