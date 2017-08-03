package com.codenotfound.kafka.integration.channel;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class CountDownLatchHandler implements MessageHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CountDownLatchHandler.class);

  private CountDownLatch latch = new CountDownLatch(10);

  public CountDownLatch getLatch() {
    return latch;
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    LOGGER.info("received message='{}'", message);
    latch.countDown();
  }
}
