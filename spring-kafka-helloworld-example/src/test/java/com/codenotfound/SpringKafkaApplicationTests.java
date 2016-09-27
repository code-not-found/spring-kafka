package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.consumer.Receiver;
import com.codenotfound.producer.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaApplicationTests {

    @Autowired
    private Sender sender;

    @Autowired
    private Receiver receiver;

    @Test
    public void testReceiver() throws Exception {
        sender.sendMessage("helloworld.t", "Hello Spring Kafka!");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);
    }
}
