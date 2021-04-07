package com.example.walletapp.controller;

import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.api.request.UserRegistrationRequest;
import com.example.walletapp.model.service.UserRegistration;
import com.example.walletapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public void register (@RequestBody UserRegistrationRequest request) {
    final UserRegistration userRegistration =
      UserRegistration.builder()
      .username(request.getUsername())
      .password(request.getPassword())
      .hkd(Optional.ofNullable(request.getHkd()).map(BigDecimal::new).orElse(BigDecimal.ZERO))
      .usd(Optional.ofNullable(request.getUsd()).map(BigDecimal::new).orElse(BigDecimal.ZERO))
      .eur(Optional.ofNullable(request.getEur()).map(BigDecimal::new).orElse(BigDecimal.ZERO))
      .build();

    final User user = userService.register(userRegistration);
  }
}
