package com.codenotfound.kafka.serializer;

import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import example.avro.User;

public class AvroDeserializerTest {

  @Test
  public void testDeserialize() {
    User user = User.newBuilder().setName("John Doe").setFavoriteColor("green")
        .setFavoriteNumber(null).build();

    byte[] data = DatatypeConverter.parseHexBinary("104A6F686E20446F6502000A677265656E");
    AvroDeserializer<User> avroDeserializer = new AvroDeserializer<>(User.class);

    assertThat(avroDeserializer.deserialize("avro-bijection.t", data)).isEqualTo(user);
    avroDeserializer.close();
  }
}
