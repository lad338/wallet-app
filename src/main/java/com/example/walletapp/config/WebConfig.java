package com.example.walletapp.config;

import com.example.walletapp.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  private final UserTokenInterceptor userTokenInterceptor;

  @Autowired
  public WebConfig(UserTokenInterceptor userTokenInterceptor) {
    this.userTokenInterceptor = userTokenInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userTokenInterceptor);
  }
}
