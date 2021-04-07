package com.example.walletapp.service.impl;

import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.UserRegistration;
import com.example.walletapp.repository.UserRepository;
import com.example.walletapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public User register(UserRegistration userRegistration) {
    final User user = User.builder()
      .username(userRegistration.getUsername())
      .password(userRegistration.getPassword())
      .hkd(userRegistration.getHkd().toString())
      .eur(userRegistration.getEur().toString())
      .usd(userRegistration.getUsd().toString())
      .build();

    return userRepository.save(user);
  }
}
