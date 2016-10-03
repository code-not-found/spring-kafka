package com.codenotfound;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@RunWith(Suite.class)
@SuiteClasses({ SpringKafkaSenderTests.class,
        SpringKafkaReceiverTests.class })
public class AllSpringKafkaTests {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AllSpringKafkaTests.class);

    protected static final String HELLOWORLD_SENDER_TOPIC = "helloworld-sender.t";
    protected static final String HELLOWORLD_RECEIVER_TOPIC = "helloworld-receiver.t";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1,
            true, HELLOWORLD_SENDER_TOPIC);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String kafkaBootstrapServers = embeddedKafka
                .getBrokersAsString();
        LOGGER.debug("kafkaServers='{}'", kafkaBootstrapServers);
        // override the property in application.properties
        System.setProperty("kafka.bootstrap.servers",
                kafkaBootstrapServers);
    }
}
