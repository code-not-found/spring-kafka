package com.codenotfound.kafka.integration.channel;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.messaging.SubscribableChannel;

@Configuration
public class ConsumingChannelConfig {

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${kafka.topic.spring-integration-kafka}")
  private String springIntegrationKafkaTopic;

  @Bean
  public SubscribableChannel consumingChannel() {
    SubscribableChannel publishSubscribeChannel = new PublishSubscribeChannel();
    publishSubscribeChannel.subscribe(countDownLatchHandler());

    return publishSubscribeChannel;
  }

  @Bean
  public CountDownLatchHandler countDownLatchHandler() {
    return new CountDownLatchHandler();
  }

  @Bean
  public KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter() {
    KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
        new KafkaMessageDrivenChannelAdapter<>(kafkaMessageListenerContainer());
    kafkaMessageDrivenChannelAdapter.setOutputChannel(consumingChannel());

    return kafkaMessageDrivenChannelAdapter;
  }

  @SuppressWarnings("unchecked")
  @Bean
  public KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer() {
    return (KafkaMessageListenerContainer<String, String>) new KafkaMessageListenerContainer<>(
        consumerFactory(),
        new ContainerProperties(new TopicPartitionInitialOffset(springIntegrationKafkaTopic, 0, 0L),
            new TopicPartitionInitialOffset(springIntegrationKafkaTopic, 1, 0L)));
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

    return properties;
  }
}
