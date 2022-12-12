package com.naskoni.orderbook.service;

import com.naskoni.orderbook.processor.crypto.CryptoProcessor;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PrintService {

  @Autowired
  private Map<String, CryptoProcessor> cryptoProcessors;

  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  @Scheduled(fixedDelayString = "${print.fixed.delay}", initialDelayString = "${print.fixed.delay}")
  public void printReports() {
    executorService.execute(getPrintingTask());
  }

  private Runnable getPrintingTask() {
    return () -> {
      for (Map.Entry<String, CryptoProcessor> entry : cryptoProcessors.entrySet()) {
        CryptoProcessor cryptoProcessor = entry.getValue();
        SortedMap<BigDecimal, String> asks = cryptoProcessor.getAsks();
        System.out.println("<------------------------------------>");
        if (!asks.isEmpty()) {
          System.out.println("asks:");
          printMap(asks);
          BigDecimal lastKey = asks.lastKey();
          System.out.println(String.format("best ask: [ %-10.5f, %s ]", lastKey, asks.get(lastKey)));
        }

        SortedMap<BigDecimal, String> bids = cryptoProcessor.getBids();
        if (!bids.isEmpty()) {
          BigDecimal firstKey = bids.firstKey();
          System.out.println(String.format("best bid: [ %-10.5f, %s ]", firstKey, bids.get(firstKey)));
          System.out.println("bids:");
          printMap(bids);
        }

        System.out.println(ZonedDateTime.now(ZoneId.of("UTC")).toInstant());
        System.out.println(entry.getKey());
        System.out.println("<------------------------------------>");
      }
    };
  }

  private void printMap(SortedMap<BigDecimal, String> data) {
    int row = 0;
    for (Map.Entry<BigDecimal, String> entry : data.entrySet()) {
      row++;
      if (row == 1) {
        System.out.println(String.format("[ [ %-10.5f, %s ]", entry.getKey(), entry.getValue()));
      } else if (row == data.size()) {
        System.out.println(String.format("  [ %-10.5f, %s ] ]", entry.getKey(), entry.getValue()));
      } else {
        System.out.println(String.format("  [ %-10.5f, %s ],", entry.getKey(), entry.getValue()));
      }
    }
  }
}
