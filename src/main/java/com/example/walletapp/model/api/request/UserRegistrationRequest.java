package com.example.walletapp.model.api.request;

import com.example.walletapp.model.service.LoginAdapter;
import lombok.Data;

@Data
public class UserRegistrationRequest implements LoginAdapter {

  private String username;
  private String password;
  private String hkd;
  private String usd;
  private String eur;
}
