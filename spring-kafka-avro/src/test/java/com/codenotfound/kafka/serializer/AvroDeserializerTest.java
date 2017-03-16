package com.codenotfound.kafka.serializer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import example.avro.User;

public class AvroDeserializerTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testDeserialize() {
    byte[] data = null;

    AvroDeserializer<User> avroDeserializer = new AvroDeserializer<>(User.class);

    assertThat(avroDeserializer.deserialize("avro.t", data)).isEqualTo("");
    avroDeserializer.close();
  }
}
