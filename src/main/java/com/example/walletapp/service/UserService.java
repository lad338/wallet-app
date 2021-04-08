package com.example.walletapp.service;

import com.example.walletapp.exception.UserNotFoundException;
import com.example.walletapp.model.service.ExecutableTransaction;
import com.example.walletapp.model.service.TransactionSnapshot;
import com.example.walletapp.model.service.UserWallet;

public interface UserService {
  UserWallet getUserWalletById(String id) throws UserNotFoundException;

  String getUserIdByUsername(String username) throws UserNotFoundException;

  void updateUserWithTransaction(
    ExecutableTransaction executableTransaction,
    TransactionSnapshot snapshot
  );
}
