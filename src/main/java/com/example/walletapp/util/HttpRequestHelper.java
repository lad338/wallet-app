package com.example.walletapp.util;

import javax.servlet.http.HttpServletRequest;

public interface HttpRequestHelper {
  default String getUserIdFromRequest(HttpServletRequest request) {
    return request.getAttribute("userId").toString();
  }
}
