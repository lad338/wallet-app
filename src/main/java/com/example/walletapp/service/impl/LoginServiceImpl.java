package com.example.walletapp.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.walletapp.config.JwtConfig;
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

  private final JwtConfig jwtConfig;

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public LoginServiceImpl(
    JwtConfig jwtConfig,
    UserRepository userRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder
  ) {
    this.jwtConfig = jwtConfig;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public String login(LoginAdapter login) {
    //get expire date
    final Date expireAt = new Date(new Date().getTime() + jwtConfig.getExpiry());

    //find the user by username
    final User user = userRepository
      .findByUsername(login.getUsername())
      .orElseThrow(InvalidLoginException::new);

    //verify the password
    if (!bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword())) {
      throw new InvalidLoginException();
    }

    //return jwt
    return JWT
      .create()
      .withIssuer("wallet-app")
      .withSubject(user.getId().toHexString())
      .withExpiresAt(expireAt)
      .sign(Algorithm.HMAC256(jwtConfig.getJwtSecret()));
  }
}
