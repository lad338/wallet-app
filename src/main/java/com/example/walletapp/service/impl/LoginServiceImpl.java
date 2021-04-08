package com.example.walletapp.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.walletapp.config.SecurityConfig;
import com.example.walletapp.exception.InvalidLoginException;
import com.example.walletapp.model.repository.User;
import com.example.walletapp.model.service.LoginAdapter;
import com.example.walletapp.repository.UserRepository;
import com.example.walletapp.service.LoginService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  private final SecurityConfig securityConfig;

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public LoginServiceImpl(
    SecurityConfig securityConfig,
    UserRepository userRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder
  ) {
    this.securityConfig = securityConfig;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public String login(LoginAdapter login) {
    final Date expireAt = new Date(new Date().getTime() + securityConfig.getExpiry());

    final User user = userRepository
      .findByUsername(login.getUsername())
      .orElseThrow(InvalidLoginException::new);

    if (!bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword())) {
      throw new InvalidLoginException();
    }

    return JWT
      .create()
      .withIssuer("wallet-app")
      .withSubject(user.getId().toHexString())
      .withExpiresAt(expireAt)
      .sign(Algorithm.HMAC256(securityConfig.getJwtSecret()));
  }
}
