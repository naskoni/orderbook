package com.naskoni.orderbook.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naskoni.orderbook.processor.crypto.CryptoProcessor;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderbookProcessorImpl implements OrderbookProcessor {

  @Autowired
  private Map<String, CryptoProcessor> cryptoProcessorMap;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void processMessage(String message) {
    try {
      Object[] messageArr = objectMapper.readValue(message, Object[].class);
      Map<String, List<List<String>>> asksAndBids = (Map<String, List<List<String>>>) messageArr[1];
      String pairName = (String) messageArr[messageArr.length - 1];
      CryptoProcessor cryptoProcessor = cryptoProcessorMap.get(pairName);
      if (cryptoProcessor != null) {
        cryptoProcessor.process(asksAndBids);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
