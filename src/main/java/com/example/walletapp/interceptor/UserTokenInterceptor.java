package com.example.walletapp.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.walletapp.annotation.UserAction;
import com.example.walletapp.config.SecurityConfig;
import com.example.walletapp.exception.InvalidUserTokenException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@Component
public class UserTokenInterceptor implements AsyncHandlerInterceptor {

  private final SecurityConfig securityConfig;

  @Autowired
  public UserTokenInterceptor(SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
  }

  @Override
  public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler
  ) {
    if (
      !(handler instanceof HandlerMethod) ||
      ((HandlerMethod) handler).getMethodAnnotation(UserAction.class) == null
    ) {
      request.setAttribute("userId", "not-required");
      return true;
    }

    final String jwt = request.getHeader("authorization").replace("Bearer ", "");
    final JWTVerifier verifier = JWT
      .require(Algorithm.HMAC256(securityConfig.getJwtSecret()))
      .build();
    try {
      final DecodedJWT verifiedJwt = verifier.verify(jwt);
      request.setAttribute("userId", verifiedJwt.getSubject());
    } catch (Exception e) {
      throw new InvalidUserTokenException();
    }
    return true;
  }
}
