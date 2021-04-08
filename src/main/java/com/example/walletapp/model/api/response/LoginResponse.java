package com.example.walletapp.model.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

  private String jwt;
}
