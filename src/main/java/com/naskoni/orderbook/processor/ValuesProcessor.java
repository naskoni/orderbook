package com.naskoni.orderbook.processor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface ValuesProcessor {

  /**
   * Processes the initial messages and add them to provided container.
   *
   * @param container container of the calling CryptoProcessor
   * @param values    initial messages
   */
  void process(SortedMap<BigDecimal, String> container, List<List<String>> values);

  /**
   * Updates the pairs in the provided container. Removes the ones with volume 0.
   *
   * @param asksMap       container for asks of the calling CryptoProcessor
   * @param bidsMap       container for bids of the calling CryptoProcessor
   * @param updateMessage update message
   */
  void update(SortedMap<BigDecimal, String> asksMap, SortedMap<BigDecimal, String> bidsMap,
      Map<String, List<List<String>>> updateMessage);
}
