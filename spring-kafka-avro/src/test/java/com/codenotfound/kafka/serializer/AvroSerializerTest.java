package com.codenotfound.kafka.serializer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import example.avro.User;

public class AvroSerializerTest {

  @Test
  public void testSerialize() {
    User user = User.newBuilder().setName("John Doe").setFavoriteColor("green")
        .setFavoriteNumber(null).build();

    AvroSerializer<User> avroSerializer = new AvroSerializer<>();
    assertThat(avroSerializer.serialize("avro.t", user)).isEqualTo("");

    avroSerializer.close();
  }
}
