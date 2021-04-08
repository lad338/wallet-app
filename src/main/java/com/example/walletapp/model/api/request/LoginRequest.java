package com.example.walletapp.model.api.request;

import com.example.walletapp.model.service.LoginAdapter;
import lombok.Data;

@Data
public class LoginRequest implements LoginAdapter {

  private String username;
  private String password;
}
