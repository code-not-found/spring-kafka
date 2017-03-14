package com.codenotfound.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.kafka.consumer.Receiver;
import com.codenotfound.kafka.producer.Sender;

import example.avro.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaApplicationTests {

  @Autowired
  private Sender sender;

  @Autowired
  private Receiver receiver;

  private User user;

  @Before
  public void setup() {
    user = User.newBuilder().setName("John Doe").setFavoriteColor("green").setFavoriteNumber(null)
        .build();
  }

  @Test
  public void testReceiver() throws Exception {
    sender.send(user);

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }
}
