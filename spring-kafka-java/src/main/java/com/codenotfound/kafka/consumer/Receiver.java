package com.codenotfound.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class Receiver implements MessageListener<String, String> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Receiver.class);

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @Override
  public void onMessage(ConsumerRecord<String, String> record) {
    LOGGER.info("received record='{}'", record);
    latch.countDown();
  }
}
