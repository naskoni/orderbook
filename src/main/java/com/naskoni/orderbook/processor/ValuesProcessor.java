package com.naskoni.orderbook.processor;

import java.math.BigDecimal;
import java.util.List;
import java.util.SortedMap;

public interface ValuesProcessor {

  void process(SortedMap<BigDecimal, String> container, List<List<String>> as);

}
