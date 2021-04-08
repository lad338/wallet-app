package com.example.walletapp.model.repository;

import com.example.walletapp.constant.Currency;
import com.example.walletapp.exception.UnexpectedException;
import com.example.walletapp.model.service.MultiCurrencyAccountAdapter;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class User implements MultiCurrencyAccountAdapter<Currency> {

  @Id
  private ObjectId id;

  @Indexed(unique = true)
  private String username;

  private String password;
  private String hkd;
  private String usd;
  private String eur;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;

  @Version
  private Integer _v;

  @Override
  public String getAmountByCurrency(Currency currency) {
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

  @Override
  public void setAmountByCurrency(Currency currency, String amount) {
    switch (currency) {
      case HKD:
        hkd = amount;
        break;
      case USD:
        usd = amount;
        break;
      case EUR:
        eur = amount;
        break;
    }
  }
}
