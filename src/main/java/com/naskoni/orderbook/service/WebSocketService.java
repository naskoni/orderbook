package com.naskoni.orderbook.service;

import com.naskoni.orderbook.processor.OrderbookProcessor;
import java.net.URI;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Slf4j
@Service
public class WebSocketService implements CommandLineRunner {

  @Value("${websocket.url}")
  private String websocketUrl;

  @Value("${websocket.subscription}")
  private String webSocketSubscription;

  @Value("${websocket.client.duration}")
  private long duration;

  @Autowired
  private OrderbookProcessor orderbookProcessor;

  public void run(String... args) {
    WebSocketClient client = new ReactorNettyWebSocketClient();
    client.execute(
            URI.create(websocketUrl),
            session -> session.send(
                    Mono.just(session.textMessage((webSocketSubscription))))
                .thenMany(session.receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .doOnEach(this::processMessage))
                .then())
        .block(Duration.ofMinutes(duration));
  }

  private void processMessage(Signal<String> signalMessage) {
    String message = signalMessage.get();
    if (message.startsWith("[")) { // this will discard subscription and heartbeat messages
      orderbookProcessor.processMessage(message);
    }
  }
}