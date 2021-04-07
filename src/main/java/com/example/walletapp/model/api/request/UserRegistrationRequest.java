package com.example.walletapp.model.api.request;

import lombok.Data;

@Data
public class UserRegistrationRequest {
  private String username;
  private String password;
  private String hkd;
  private String usd;
  private String eur;
}
