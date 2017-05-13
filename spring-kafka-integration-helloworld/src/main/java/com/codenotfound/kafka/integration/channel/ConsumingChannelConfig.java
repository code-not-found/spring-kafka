package com.codenotfound.kafka.integration.channel;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

@Configuration
public class ConsumingChannelConfig {

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${kafka.topic.spring-integration-kafka}")
  private String springIntegrationKafkaTopic;

  @Bean
  public DirectChannel consumingChannel() {
    return new DirectChannel();
  }

  @Bean
  public KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter() {
    KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
        new KafkaMessageDrivenChannelAdapter<>(kafkaListenerContainer());
    kafkaMessageDrivenChannelAdapter.setOutputChannel(consumingChannel());

    return kafkaMessageDrivenChannelAdapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "consumingChannel")
  public CountDownLatchHandler countDownLatchHandler() {
    return new CountDownLatchHandler();
  }

  @SuppressWarnings("unchecked")
  @Bean
  public ConcurrentMessageListenerContainer<String, String> kafkaListenerContainer() {
    ContainerProperties containerProps = new ContainerProperties(springIntegrationKafkaTopic);

    return (ConcurrentMessageListenerContainer<String, String>) new ConcurrentMessageListenerContainer<>(
        consumerFactory(), containerProps);
  }

  @Bean
  public ConsumerFactory<?, ?> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "spring-integration");
    // automatically reset the offset to the earliest offset
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    return properties;
  }
}
