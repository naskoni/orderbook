package com.naskoni.orderbook.processor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface ValuesProcessor {

  void process(SortedMap<BigDecimal, String> container, List<List<String>> values);

  void update(SortedMap<BigDecimal, String> asksMap, SortedMap<BigDecimal, String> bidsMap, Map<String, List<List<String>>> updateMessage);
}
