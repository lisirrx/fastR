package me.lisirrx.fastR.serialization;

import io.rsocket.Payload;
import me.lisirrx.fastR.api.Message;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
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
        Crypt c = CryptFactory.createCrypt("DES", "1");

        HessianCodec codec = new HessianCodec(c);
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

    @Test
    void encryp() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException {
        DESKeySpec desKeySpec = new DESKeySpec("hedddddd".getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES");
// 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte[] bytes = cipher.doFinal("world".getBytes());

        Cipher cipher1 = Cipher.getInstance("DES");
        cipher1.init(Cipher.DECRYPT_MODE, securekey);
        byte[] bytes1 = cipher1.doFinal(bytes);
        System.out.println(bytes1);
    }
}