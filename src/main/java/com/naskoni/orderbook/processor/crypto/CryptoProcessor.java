package com.naskoni.orderbook.processor.crypto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface CryptoProcessor {

  void process(Map<String, List<List<String>>> asksAndBids);

  SortedMap<BigDecimal, String> getAsks();

  SortedMap<BigDecimal, String> getBids();
}
