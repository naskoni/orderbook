package com.naskoni.orderbook.processor.crypto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface CryptoProcessor {

  /**
   * Process initial messages and update messages from Kraken
   *
   * @param asksAndBids initial messages and update messages
   */
  void process(Map<String, List<List<String>>> asksAndBids);

  /**
   * Should return the copy of the container for asks
   *
   * @return the container for asks
   */
  SortedMap<BigDecimal, String> getAsks();

  /**
   * Should return the copy of the container for bids
   *
   * @return the container for bids
   */
  SortedMap<BigDecimal, String> getBids();
}
