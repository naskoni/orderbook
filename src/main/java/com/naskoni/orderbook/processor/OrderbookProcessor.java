package com.naskoni.orderbook.processor;

public interface OrderbookProcessor {

  /**
   * Processes messages from Kraken WebSockets API, delegates to appropriate CryptoProcessor.
   *
   * @param message message from Kraken WebSockets API
   */
  void processMessage(String message);
}
