package com.example.walletapp.controller;

import com.example.walletapp.model.api.request.LoginRequest;
import com.example.walletapp.model.api.response.LoginResponse;
import com.example.walletapp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private final LoginService loginService;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    final String jwt = loginService.login(request);

    final LoginResponse response = LoginResponse.builder().jwt(jwt).build();

    return ResponseEntity.ok(response);
  }
}
