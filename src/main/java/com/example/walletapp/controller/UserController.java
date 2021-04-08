package com.example.walletapp.controller;

import com.example.walletapp.annotation.UserAction;
import com.example.walletapp.model.api.request.UserRegistrationRequest;
import com.example.walletapp.model.api.response.LoginResponse;
import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.UserRegistration;
import com.example.walletapp.model.service.UserWallet;
import com.example.walletapp.service.LoginService;
import com.example.walletapp.service.UserService;
import com.example.walletapp.util.BigDecimalHelper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController implements BigDecimalHelper {

  private final UserService userService;
  private final LoginService loginService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserController(
    UserService userService,
    LoginService loginService,
    BCryptPasswordEncoder bCryptPasswordEncoder
  ) {
    this.userService = userService;
    this.loginService = loginService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<LoginResponse> register(@RequestBody UserRegistrationRequest request) {
    final UserRegistration userRegistration = UserRegistration
      .builder()
      .username(request.getUsername())
      .password(bCryptPasswordEncoder.encode(request.getPassword()))
      .hkd(getDecimalFromString(request.getHkd()))
      .usd(getDecimalFromString(request.getUsd()))
      .eur(getDecimalFromString(request.getEur()))
      .build();
    final User user = userService.register(userRegistration);

    final String jwt = loginService.login(request);
    final LoginResponse response = LoginResponse.builder().jwt(jwt).build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  @ResponseBody
  @UserAction
  public ResponseEntity<UserWallet> myWallet(HttpServletRequest request) {
    final String userId = request.getAttribute("userId").toString();
    final UserWallet userWallet = userService.getUserWalletById(userId);

    return ResponseEntity.ok(userWallet);
  }
}
