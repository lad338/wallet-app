package com.example.walletapp.controller;

import com.example.walletapp.annotation.UserAction;
import com.example.walletapp.constant.Currency;
import com.example.walletapp.constant.TransactionAction;
import com.example.walletapp.model.api.request.TransactionRequest;
import com.example.walletapp.model.api.request.UserRegistrationRequest;
import com.example.walletapp.model.api.response.LoginResponse;
import com.example.walletapp.model.api.response.TransactionHistoryResponse;
import com.example.walletapp.model.api.response.TransactionResponse;
import com.example.walletapp.model.repository.Transaction;
import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.ExecutableTransaction;
import com.example.walletapp.model.service.UserRegistration;
import com.example.walletapp.model.service.UserWallet;
import com.example.walletapp.service.LoginService;
import com.example.walletapp.service.RegistrationService;
import com.example.walletapp.service.TransactionService;
import com.example.walletapp.service.UserService;
import com.example.walletapp.util.BigDecimalHelper;
import com.example.walletapp.util.HttpRequestHelper;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController implements BigDecimalHelper, HttpRequestHelper {

  private final UserService userService;
  private final LoginService loginService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final TransactionService transactionService;
  private final RegistrationService registrationService;

  @Autowired
  public UserController(
    UserService userService,
    LoginService loginService,
    TransactionService transactionService,
    BCryptPasswordEncoder bCryptPasswordEncoder,
    RegistrationService registrationService
  ) {
    this.userService = userService;
    this.loginService = loginService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.transactionService = transactionService;
    this.registrationService = registrationService;
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

    final User user = registrationService.register(userRegistration);

    final String jwt = loginService.login(request);
    final LoginResponse response = LoginResponse.builder().jwt(jwt).build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  @ResponseBody
  @UserAction
  public ResponseEntity<UserWallet> myWallet(HttpServletRequest request) {
    final String userId = getUserIdFromRequest(request);
    final UserWallet userWallet = userService.getUserWalletById(userId);

    return ResponseEntity.ok(userWallet);
  }

  @PostMapping("/me/transactions")
  @ResponseBody
  @UserAction
  public ResponseEntity<TransactionResponse> transaction(
    HttpServletRequest httpRequest,
    @RequestBody TransactionRequest transactionRequest
  ) {
    final String userId = getUserIdFromRequest(httpRequest);
    final String targetId = TransactionAction.TRANSFER.equals(transactionRequest.getAction())
      ? userService.getUserIdByUsername(transactionRequest.getTargetUsername())
      : null;
    final BigDecimal amount = getDecimalFromString(transactionRequest.getAmount());
    final Currency currency = transactionRequest.getCurrency();
    final Currency targetCurrency = transactionRequest.getTargetCurrency();
    final TransactionAction action = transactionRequest.getAction();

    final ExecutableTransaction executableTransaction = ExecutableTransaction
      .builder()
      .userId(userId)
      .targetId(targetId)
      .amount(amount)
      .currency(currency)
      .targetCurrency(targetCurrency)
      .action(action)
      .build();

    final String transactionId = transactionService
      .executeTransaction(executableTransaction)
      .getId()
      .toHexString();
    final TransactionResponse response = TransactionResponse
      .builder()
      .transactionId(transactionId)
      .build();

    return ResponseEntity.accepted().body(response);
  }

  @GetMapping("/me/transactions")
  @ResponseBody
  @UserAction
  public ResponseEntity<TransactionHistoryResponse> transactionHistory(
    HttpServletRequest httpRequest
  ) {
    final String userId = getUserIdFromRequest(httpRequest);

    final List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);

    final List<TransactionHistoryResponse.TransactionRecord> transactionRecords = transactions
      .stream()
      .map(
        transaction ->
          TransactionHistoryResponse.TransactionRecord.fromTransaction(transaction, userId)
      )
      .collect(Collectors.toList());

    final TransactionHistoryResponse response = TransactionHistoryResponse
      .builder()
      .transactionRecords(transactionRecords)
      .build();

    return ResponseEntity.ok(response);
  }
}
