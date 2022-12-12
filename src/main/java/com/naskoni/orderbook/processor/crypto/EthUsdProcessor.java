package com.naskoni.orderbook.processor.crypto;

import com.naskoni.orderbook.processor.ValuesProcessor;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ETH/USD")
public class EthUsdProcessor implements CryptoProcessor {

  @Autowired
  private ValuesProcessor valuesProcessor;

  private SortedMap<BigDecimal, String> asksMap = new TreeMap<>(Collections.reverseOrder());
  private SortedMap<BigDecimal, String> bidsMap = new TreeMap<>(Collections.reverseOrder());

  @Override
  public void process(Map<String, List<List<String>>> asksAndBids) {
    if (asksAndBids.containsKey("as")) {
      valuesProcessor.process(asksMap, asksAndBids.get("as"));
      valuesProcessor.process(bidsMap, asksAndBids.get("bs"));
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
