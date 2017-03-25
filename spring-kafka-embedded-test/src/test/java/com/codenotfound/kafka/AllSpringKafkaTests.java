package com.codenotfound.kafka;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@RunWith(Suite.class)
@SuiteClasses({SpringKafkaSenderTest.class, SpringKafkaReceiverTest.class})
public class AllSpringKafkaTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllSpringKafkaTests.class);

  protected static final String SENDER_TOPIC = "sender.t";
  protected static final String RECEIVER_TOPIC = "receiver.t";

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SENDER_TOPIC);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    String kafkaBootstrapServers = embeddedKafka.getBrokersAsString();
    LOGGER.debug("kafkaServers='{}'", kafkaBootstrapServers);
    // override the property in application.properties
    System.setProperty("kafka.servers.bootstrap", kafkaBootstrapServers);
  }
}
