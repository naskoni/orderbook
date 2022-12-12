package com.naskoni.orderbook.processor;

import java.math.BigDecimal;
import java.util.List;

public class Constants {

  public static final String PRICE = "1266.88000";
  public static final BigDecimal PRICE_BD = new BigDecimal(PRICE);
  public static final String VOLUME = "102.36729861";
  public static final List<String> VALUE = List.of(PRICE, VOLUME);

  private Constants() {
  }
}
