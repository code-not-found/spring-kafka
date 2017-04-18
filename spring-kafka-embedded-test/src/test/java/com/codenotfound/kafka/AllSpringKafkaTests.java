package com.codenotfound.kafka;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;

import com.codenotfound.kafka.consumer.SpringKafkaReceiverTest;
import com.codenotfound.kafka.producer.SpringKafkaSenderTest;

@RunWith(Suite.class)
@SuiteClasses({SpringKafkaApplicationTest.class, SpringKafkaSenderTest.class,
    SpringKafkaReceiverTest.class})
public class AllSpringKafkaTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllSpringKafkaTests.class);

  public static final String SENDER_TOPIC = "sender.t";
  public static final String RECEIVER_TOPIC = "receiver.t";

  @ClassRule
  public static KafkaEmbedded embeddedKafka =
      new KafkaEmbedded(1, true, SENDER_TOPIC, RECEIVER_TOPIC);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    String kafkaBootstrapServers = embeddedKafka.getBrokersAsString();

    LOGGER.debug("kafkaServers='{}'", kafkaBootstrapServers);
    // override the property in application.properties
    System.setProperty("kafka.bootstrap-servers", kafkaBootstrapServers);
  }
}
