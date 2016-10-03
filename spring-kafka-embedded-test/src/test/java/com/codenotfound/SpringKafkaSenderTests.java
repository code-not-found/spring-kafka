package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.value;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.producer.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaSenderTests {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SpringKafkaSenderTests.class);

    @Autowired
    private Sender sender;

    @Test
    public void testSender() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties = KafkaTestUtils
                .consumerProps("helloworld_sender_group", "false",
                        AllSpringKafkaTests.embeddedKafka);

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<Integer, String>(
                consumerProperties);
        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(
                AllSpringKafkaTests.HELLOWORLD_SENDER_TOPIC);

        // create a Kafka MessageListenerContainer
        KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(
                consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        BlockingQueue<ConsumerRecord<Integer, String>> records = new LinkedBlockingQueue<>();
        // setup a Kafka message listener
        container.setupMessageListener(
                new MessageListener<Integer, String>() {
                    @Override
                    public void onMessage(
                            ConsumerRecord<Integer, String> record) {
                        LOGGER.debug(record.toString());
                        records.add(record);
                    }
                });

        // start the container and underlying message listener
        container.start();
        // wait until the container has the required number of assigned
        // partitions
        ContainerTestUtils.waitForAssignment(container,
                AllSpringKafkaTests.embeddedKafka
                        .getPartitionsPerTopic());

        // send the message
        sender.sendMessage(AllSpringKafkaTests.HELLOWORLD_SENDER_TOPIC,
                "Hello Spring Kafka Sender!");
        // check that the message was received
        assertThat(records.poll(10, TimeUnit.SECONDS))
                .has(value("Hello Spring Kafka Sender!"));

        // stop the container
        container.stop();
    }
}
