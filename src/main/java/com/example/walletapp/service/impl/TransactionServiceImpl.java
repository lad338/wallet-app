package com.example.walletapp.service.impl;

import com.example.walletapp.constant.TransactionAction;
import com.example.walletapp.model.repository.Transaction;
import com.example.walletapp.model.service.ExecutableTransaction;
import com.example.walletapp.model.service.TransactionSnapshot;
import com.example.walletapp.repository.ExchangeRateRepository;
import com.example.walletapp.repository.TransactionRepository;
import com.example.walletapp.service.TransactionService;
import com.example.walletapp.service.UserService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final UserService userService;

  private final ExchangeRateRepository exchangeRateRepository;

  @Autowired
  public TransactionServiceImpl(
    TransactionRepository transactionRepository,
    UserService userService,
    ExchangeRateRepository exchangeRateRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.userService = userService;
    this.exchangeRateRepository = exchangeRateRepository;
  }

  @Override
  @Transactional
  public Transaction executeTransaction(ExecutableTransaction transaction) {
    final BigDecimal requiredExchangeRate = transaction
        .getAction()
        .equals(TransactionAction.EXCHANGE)
      ? exchangeRateRepository.getExchangeRate(
        transaction.getCurrency(),
        transaction.getTargetCurrency()
      )
      : null;

    final BigDecimal exchangedAmount = Optional
      .ofNullable(requiredExchangeRate)
      .map(
        exchangeRate ->
          exchangeRate.multiply(transaction.getAmount()).setScale(4, RoundingMode.HALF_UP)
      )
      .orElse(null);

    final TransactionSnapshot snapshot = TransactionSnapshot
      .builder()
      .exchangedAmount(exchangedAmount)
      .exchangeRate(requiredExchangeRate)
      .time(new Date())
      .build();

    userService.updateUserWithTransaction(transaction, snapshot);

    final Transaction transactionHistory = Transaction
      .builder()
      .transactionAction(transaction.getAction().toString())
      .userId(transaction.getUserId())
      .targetId(transaction.getTargetId())
      .amount(transaction.getAmount().toString())
      .currency(transaction.getCurrency().toString())
      .targetCurrency(
        Optional.ofNullable(transaction.getTargetCurrency()).map(Objects::toString).orElse(null)
      )
      .exchangeRate(
        Optional.ofNullable(snapshot.getExchangeRate()).map(BigDecimal::toString).orElse(null)
      )
      .targetAmount(
        Optional.ofNullable(snapshot.getExchangedAmount()).map(BigDecimal::toString).orElse(null)
      )
      .executedAt(snapshot.getTime())
      .build();

    return transactionRepository.save(transactionHistory);
  }

  @Override
  public List<Transaction> getTransactionsByUserId(String userId) {
    return transactionRepository.getTransactionsByUserIdOrTargetIdOrderByExecutedAt(userId, userId);
  }
}
