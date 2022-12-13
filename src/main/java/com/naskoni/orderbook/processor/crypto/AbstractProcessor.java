package com.naskoni.orderbook.processor.crypto;

import com.naskoni.orderbook.processor.ValuesProcessor;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Every class that extends this abstract class should have @Component("{pair}"). Format of each pair is "A/B", where A and B are
 * ISO 4217-A3 for standardized assets and popular unique symbol if not standardized.
 */
public abstract class AbstractProcessor implements CryptoProcessor {

  @Autowired
  private ValuesProcessor valuesProcessor;

  private SortedMap<BigDecimal, String> asksMap = new TreeMap<>(Collections.reverseOrder());
  private SortedMap<BigDecimal, String> bidsMap = new TreeMap<>(Collections.reverseOrder());

  @Override
  public void process(Map<String, List<List<String>>> asksAndBids) {
    Objects.requireNonNull(asksAndBids);
    if (asksAndBids.containsKey("as")) {
      valuesProcessor.process(asksMap, asksAndBids.get("as"));
      valuesProcessor.process(bidsMap, asksAndBids.get("bs"));
    } else if (asksAndBids.containsKey("b") || asksAndBids.containsKey("a")) {
      valuesProcessor.update(asksMap, bidsMap, asksAndBids);
    }
  }

  @Override
  public SortedMap<BigDecimal, String> getAsks() {
    return new TreeMap<>(asksMap);
  }

  @Override
  public SortedMap<BigDecimal, String> getBids() {
    return new TreeMap<>(bidsMap);
  }
}
