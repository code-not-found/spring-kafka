package com.codenotfound.kafka.consumer;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class Receiver {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Receiver.class);

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @KafkaListener(topics = "receiver.t")
  public void receive(String payload) {
    LOGGER.info("received payload='{}'", payload);
    latch.countDown();
  }
}
