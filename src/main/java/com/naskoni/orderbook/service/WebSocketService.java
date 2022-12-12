package com.naskoni.orderbook.service;

import com.naskoni.orderbook.processor.OrderbookProcessor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketService implements CommandLineRunner {

  Logger logger = LoggerFactory.getLogger(WebSocketService.class);

  @Value("${websocket.url}")
  private String websocketUrl;

  @Value("${websocket.subscription}")
  private String webSocketSubscription;

  @Autowired
  private OrderbookProcessor orderbookProcessor;

  public void run(String... args) {
    try {
      CountDownLatch latch = new CountDownLatch(1);
      HttpClient client = HttpClient.newHttpClient();
      CompletableFuture<WebSocket> ws = client
          .newWebSocketBuilder()
          .buildAsync(URI.create(websocketUrl), new WebSocketListener(orderbookProcessor));

      WebSocket webSocket = ws.get();
      webSocket.sendText(webSocketSubscription, true);
      latch.await();
    } catch (InterruptedException e) {
      logger.error("WebSocket creation failed, the thread was interrupted: ", e);
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      logger.error("WebSocket creation failed: ", e);
    }
  }

  private static class WebSocketListener implements WebSocket.Listener {

    private final OrderbookProcessor orderbookProcessor;

    public WebSocketListener(OrderbookProcessor orderbookProcessor) {
      this.orderbookProcessor = orderbookProcessor;
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
      processMessage(data.toString());
      return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
      System.out.println("Error occured! " + error.getMessage());
    }

    private void processMessage(String message) {
      if (message.startsWith("[")) { // this will discard subscription and heartbeat messages
        orderbookProcessor.processMessage(message);
      }
    }
  }
}
