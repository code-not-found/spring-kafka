package com.codenotfound.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;

import com.codenotfound.kafka.consumer.Receiver;
import com.codenotfound.kafka.consumer.ReceiverConfig;
import com.codenotfound.kafka.producer.SenderConfig;

public class SpringKafkaMainTest {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SpringKafkaMainTest.class);

  private final static String JAVA_TOPIC = "topic1";

  @ClassRule
  public static KafkaEmbedded embeddedKafka =
      new KafkaEmbedded(1, true, JAVA_TOPIC);

  @Test
  public void testReceive() throws Exception {
    String bootstrapServers = embeddedKafka.getBrokersAsString();
    LOGGER.info("bootstrapServers='{}'", bootstrapServers);

    ContainerProperties containerProperties =
        new ContainerProperties(JAVA_TOPIC);
    Receiver receiver = new Receiver();

    KafkaMessageListenerContainer<String, String> messageListenerContainer =
        ReceiverConfig.createMessageListenerContainer(
            containerProperties, bootstrapServers);
    messageListenerContainer.setupMessageListener(receiver);
    messageListenerContainer.start();

    // wait a bit for the container to start
    Thread.sleep(1000);

    KafkaTemplate<String, String> kafkaTemplate =
        SenderConfig.createKafkaTemplate(bootstrapServers);
    kafkaTemplate.send(JAVA_TOPIC, "Hello Java!");

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);

    messageListenerContainer.stop();
  }
}
