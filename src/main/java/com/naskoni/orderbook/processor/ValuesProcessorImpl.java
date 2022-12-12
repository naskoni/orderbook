package com.naskoni.orderbook.processor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import org.springframework.stereotype.Component;

@Component
public class ValuesProcessorImpl implements ValuesProcessor {

  @Override
  public void process(SortedMap<BigDecimal, String> container, List<List<String>> values) {
    values.forEach(v -> putToContainer(v, container));
  }

  @Override
  public void update(SortedMap<BigDecimal, String> asksMap, SortedMap<BigDecimal, String> bidsMap, Map<String, List<List<String>>> updateMessage) {
    if (updateMessage.containsKey("a")) {
      List<List<String>> asksUpdates = updateMessage.get("a");
      asksUpdates.forEach(u -> processUpdate(u, asksMap));
    }

    if (updateMessage.containsKey("b")) {
      List<List<String>> bidsUpdates = updateMessage.get("b");
      bidsUpdates.forEach(u -> processUpdate(u, bidsMap));
    }
  }

  private void processUpdate(List<String> update, SortedMap<BigDecimal, String> container) {
    BigDecimal price = getPrice(update);
    String volume = getVolume(update);
    if (Double.parseDouble(volume) == 0d) {
      container.remove(price);
    } else {
      container.put(price, volume);
    }
  }

  private void putToContainer(List<String> values, SortedMap<BigDecimal, String> container) {
    if (values.size() > 1) {
      container.put(getPrice(values), getVolume(values));
    }
  }

  private BigDecimal getPrice(List<String> values) {
    return new BigDecimal(values.get(0));
  }

  private String getVolume(List<String> values) {
    return values.get(1);
  }
}
