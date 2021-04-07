package com.example.walletapp.service;

import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.UserRegistration;

public interface UserService {
  User register(UserRegistration userRegistration);
}
