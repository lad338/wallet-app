package com.example.walletapp.service;

import com.example.walletapp.model.repository.Transaction;
import com.example.walletapp.model.service.ExecutableTransaction;
import java.util.List;

public interface TransactionService {
  Transaction executeTransaction(ExecutableTransaction transaction);
  List<Transaction> getTransactionsByUserId(String userId);
  List<Transaction> getPageableTransactionsByUserId(String userId, Integer page, Integer size);
}
