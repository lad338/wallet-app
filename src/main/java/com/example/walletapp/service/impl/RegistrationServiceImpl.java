package com.example.walletapp.service.impl;

import com.example.walletapp.constant.Currency;
import com.example.walletapp.constant.TransactionAction;
import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.ExecutableTransaction;
import com.example.walletapp.model.service.UserRegistration;
import com.example.walletapp.repository.UserRepository;
import com.example.walletapp.service.RegistrationService;
import com.example.walletapp.service.TransactionService;
import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

  private final UserRepository userRepository;
  private final TransactionService transactionService;

  @Autowired
  public RegistrationServiceImpl(
    UserRepository userRepository,
    TransactionService transactionService
  ) {
    this.userRepository = userRepository;
    this.transactionService = transactionService;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public User register(UserRegistration userRegistration) {
    final User user = User
      .builder()
      .username(userRegistration.getUsername())
      .password(userRegistration.getPassword())
      .hkd("0")
      .eur("0")
      .usd("0")
      .build();

    final User createdUser = userRepository.save(user);

    Arrays
      .asList(Currency.values())
      .forEach(
        currency -> {
          final BigDecimal amount = userRegistration.getAmountByCurrency(currency);
          if (BigDecimal.ZERO.compareTo(amount) < 0) {
            final ExecutableTransaction executableTransaction = ExecutableTransaction
              .builder()
              .userId(createdUser.getId().toHexString())
              .amount(amount)
              .currency(currency)
              .action(TransactionAction.DEPOSIT)
              .build();
            transactionService.executeTransaction(executableTransaction);
          }
        }
      );

    return createdUser;
  }
}
