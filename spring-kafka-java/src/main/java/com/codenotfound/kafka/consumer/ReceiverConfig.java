package com.codenotfound.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

public class ReceiverConfig {

  private ReceiverConfig() {}

  public static KafkaMessageListenerContainer<String, String> createMessageListenerContainer(
      ContainerProperties containerProperties,
      String bootstrapServers) {
    return new KafkaMessageListenerContainer<>(
        createConsumerFactory(bootstrapServers), containerProperties);
  }

  private static ConsumerFactory<String, String> createConsumerFactory(
      String bootstrapServers) {
    return new DefaultKafkaConsumerFactory<>(
        createConsumerConfig(bootstrapServers));
  }

  private static Map<String, Object> createConsumerConfig(
      String bootstrapServers) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "java");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        "earliest");

    return properties;
  }
}
