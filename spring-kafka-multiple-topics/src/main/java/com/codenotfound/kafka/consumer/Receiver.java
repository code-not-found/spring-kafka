package com.codenotfound.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import com.codenotfound.model.Bar;
import com.codenotfound.model.Foo;

public class Receiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

  private CountDownLatch latch = new CountDownLatch(2);

  public CountDownLatch getLatch() {
    return latch;
  }

  @KafkaListener(topics = "${kafka.topic.bar}")
  public void receiveBar(Bar bar) {
    LOGGER.info("received {}", bar.toString());
    latch.countDown();
  }

  @KafkaListener(topics = "${kafka.topic.foo}")
  public void receiveFoo(Foo foo) {
    LOGGER.info("received {}", foo.toString());
    latch.countDown();
  }
}
