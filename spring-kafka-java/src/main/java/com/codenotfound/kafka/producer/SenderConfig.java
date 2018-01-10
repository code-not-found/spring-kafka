package com.codenotfound.kafka.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

public class SenderConfig {

  private SenderConfig() {}

  public static KafkaTemplate<String, String> createKafkaTemplate(
      String bootstrapServers) {
    return new KafkaTemplate<>(
        createProducerFactory(bootstrapServers));
  }

  private static ProducerFactory<String, String> createProducerFactory(
      String bootstrapServers) {
    return new DefaultKafkaProducerFactory<>(
        createProducerConfig(bootstrapServers));
  }

  private static Map<String, Object> createProducerConfig(
      String bootstrapServers) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);

    return properties;
  }
}
