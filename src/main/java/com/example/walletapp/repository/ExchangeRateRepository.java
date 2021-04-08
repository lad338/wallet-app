package com.example.walletapp.repository;

import com.example.walletapp.constant.Currency;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ExchangeRateRepository {

  public BigDecimal getExchangeRate(Currency fromCurrency, Currency toCurrency) {
    final double baseExchangeRate = BASE_EXCHANGE_RATE.get(fromCurrency).get(toCurrency);
    return randomizer(new BigDecimal(baseExchangeRate)).setScale(4, RoundingMode.HALF_UP);
  }

  private static BigDecimal randomizer(BigDecimal input) {
    final double floating = 0.9 + 0.2 * Math.random();
    return input.multiply(new BigDecimal(floating));
  }

  private static final Map<Currency, Map<Currency, Double>> BASE_EXCHANGE_RATE = ImmutableMap.of(
    Currency.HKD,
    ImmutableMap.of(Currency.USD, 0.13, Currency.EUR, 0.11),
    Currency.USD,
    ImmutableMap.of(Currency.HKD, 7.78, Currency.EUR, 0.84),
    Currency.EUR,
    ImmutableMap.of(Currency.HKD, 9.23, Currency.USD, 1.19)
  );
}
