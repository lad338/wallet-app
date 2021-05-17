package com.example.walletapp.controller;

import com.example.walletapp.annotation.UserAction;
import com.example.walletapp.service.TransactionService;
import com.example.walletapp.util.HttpRequestHelper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController implements HttpRequestHelper {

  private final TransactionService transactionService;

  @Autowired
  public TestController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping("/test")
  @UserAction
  public ResponseEntity<Object> test(HttpServletRequest request) {
    final String userId = getUserIdFromRequest(request);

    return ResponseEntity.ok(transactionService.getPageableTransactionsByUserId(userId, 0, 3));
  }
}
