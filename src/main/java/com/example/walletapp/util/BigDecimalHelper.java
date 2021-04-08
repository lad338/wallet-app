package com.example.walletapp.util;

import java.math.BigDecimal;
import java.util.Optional;

public interface BigDecimalHelper {
  default BigDecimal getDecimalFromString(String s) {
    return Optional.ofNullable(s).map(BigDecimal::new).orElse(BigDecimal.ZERO);
  }
}
