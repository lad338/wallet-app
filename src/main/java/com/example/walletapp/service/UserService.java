package com.example.walletapp.service;

import com.example.walletapp.exception.UserNotFoundException;
import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.UserRegistration;
import com.example.walletapp.model.service.UserWallet;

public interface UserService {
  User register(UserRegistration userRegistration);

  UserWallet getUserWalletById(String id) throws UserNotFoundException;
}
