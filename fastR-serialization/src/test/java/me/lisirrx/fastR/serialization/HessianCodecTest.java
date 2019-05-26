package me.lisirrx.fastR.serialization;

import io.rsocket.Payload;
import me.lisirrx.fastR.api.Message;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HessianCodecTest {


    @Test
    void encode() {
        TestData data = new TestData("hello");

        Map<String, String> header = new HashMap<>();
        header.put("key1", "hi");
        Message message = new Message(header, data);

        HessianCodec codec = new HessianCodec();
        Payload payload = codec.encode(message);

        Message messageNew = codec.decode(payload);

        assertEquals("hello", ((TestData)messageNew.getData()).getInner());
        assertEquals("hi", messageNew.getHeader("key1"));
    }

    @Test
    void decode() {

        Mono.justOrEmpty("dsf")
                .switchIfEmpty(Mono.just("12312"))
                .subscribe(System.out::print);
    }
}