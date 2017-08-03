package com.codenotfound.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;

public class Sender {

  private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

  @Autowired
  private KafkaTemplate<String, ?> kafkaTemplate;

  public void send(String topic, Object payload) {
    LOGGER.info("sending payload='{}' to topic='{}'", payload.toString(), topic);
    kafkaTemplate
        .send(MessageBuilder.withPayload(payload).setHeader(KafkaHeaders.TOPIC, topic).build());
  }
}
