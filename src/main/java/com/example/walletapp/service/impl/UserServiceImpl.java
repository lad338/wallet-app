package com.example.walletapp.service.impl;

import com.example.walletapp.constant.Currency;
import com.example.walletapp.exception.InsufficientAmountException;
import com.example.walletapp.exception.UnexpectedException;
import com.example.walletapp.exception.UserNotFoundException;
import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.ExecutableTransaction;
import com.example.walletapp.model.service.TransactionSnapshot;
import com.example.walletapp.model.service.UserWallet;
import com.example.walletapp.repository.UserRepository;
import com.example.walletapp.service.UserService;
import com.example.walletapp.util.BigDecimalHelper;
import java.math.BigDecimal;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, BigDecimalHelper {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserWallet getUserWalletById(String id) throws UserNotFoundException {
    return userRepository
      .findById(new ObjectId(id))
      .map(
        user ->
          UserWallet
            .builder()
            .usd(getDecimalFromString(user.getUsd()))
            .hkd(getDecimalFromString(user.getHkd()))
            .eur(getDecimalFromString(user.getEur()))
            .build()
      )
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public String getUserIdByUsername(String username) throws UserNotFoundException {
    return userRepository
      .findByUsername(username)
      .map(user -> user.getId().toHexString())
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  @Transactional
  public void updateUserWithTransaction(
    ExecutableTransaction executableTransaction,
    TransactionSnapshot snapshot
  ) {
    final User user = userRepository
      .findById(new ObjectId(executableTransaction.getUserId()))
      .orElseThrow(UserNotFoundException::new);

    final Currency currency = executableTransaction.getCurrency();
    final BigDecimal amount = executableTransaction.getAmount();

    switch (executableTransaction.getAction()) {
      case DEPOSIT:
        {
          updateAccount(user, currency, amount, UpdateAccountAction.ADD);
          break;
        }
      case WITHDRAW:
        {
          requireUserSufficientAmount(user, currency, amount);
          updateAccount(user, currency, amount, UpdateAccountAction.DEDUCT);
          break;
        }
      case EXCHANGE:
        {
          requireUserSufficientAmount(user, currency, amount);

          final Currency targetCurrency = executableTransaction.getTargetCurrency();
          final BigDecimal targetAmount = snapshot.getExchangedAmount();

          updateAccount(user, currency, amount, UpdateAccountAction.DEDUCT);
          updateAccount(user, targetCurrency, targetAmount, UpdateAccountAction.ADD);
          break;
        }
      case TRANSFER:
        {
          requireUserSufficientAmount(user, currency, amount);

          try {
            final User target = requireExistingTarget(executableTransaction.getTargetId());
            updateAccount(target, currency, amount, UpdateAccountAction.ADD);
            userRepository.save(target);
          } catch (UserNotFoundException e) {
            throw new UnexpectedException("Target User Id Not Found");
          }
          updateAccount(user, currency, amount, UpdateAccountAction.DEDUCT);
          break;
        }
    }
    userRepository.save(user);
  }

  private void updateAccount(
    User user,
    Currency currency,
    BigDecimal amount,
    UpdateAccountAction action
  ) {
    if (!UpdateAccountAction.ADD.equals(action)) {
      updateAccount(user, currency, amount.negate(), UpdateAccountAction.ADD);
      return;
    }
    final BigDecimal updatedAmount = new BigDecimal(user.getAmountByCurrency(currency)).add(amount);
    user.setAmountByCurrency(currency, updatedAmount.toString());
  }

  private enum UpdateAccountAction {
    ADD,
    DEDUCT,
  }

  private void requireUserSufficientAmount(User user, Currency currency, BigDecimal amount) {
    if (new BigDecimal(user.getAmountByCurrency(currency)).compareTo(amount) < 0) {
      throw new InsufficientAmountException();
    }
  }

  private User requireExistingTarget(String targetId) throws UserNotFoundException {
    return userRepository.findById(new ObjectId(targetId)).orElseThrow(UserNotFoundException::new);
  }
}
