package com.codenotfound.consumer;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;

public class Receiver {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.receiver.topic}")
    public void receiveMessage(String message) {
        LOGGER.info("received message='{}'", message);
        latch.countDown();
    }
    
    @EventListener()
    public void eventHandler(ListenerContainerIdleEvent event) {
        LOGGER.debug("event='{}'", event.toString());
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
