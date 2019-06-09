package me.lisirrx.fastR.serialization;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.Hessian2StreamingOutput;
import io.rsocket.Payload;
import io.rsocket.util.ByteBufPayload;
import me.lisirrx.fastR.api.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.CryptoPrimitive;
import java.util.HashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */
public class HessianCodec implements Codec {

    private Crypt crypt;

    public HessianCodec(Crypt crypt) {
        this.crypt = crypt;
    }

    @Override
    public Payload encode(Message message) {

        message.setHeader("Crypt", crypt.cryptSign());

        ByteArrayOutputStream streamData = new ByteArrayOutputStream();
        Hessian2StreamingOutput hessianData = new Hessian2StreamingOutput(streamData);

        ByteArrayOutputStream streamHeader = new ByteArrayOutputStream();
        Hessian2StreamingOutput hessianHeader = new Hessian2StreamingOutput(streamHeader);
        try {
            hessianData.writeObject(message.getData());
            hessianHeader.writeObject(message.getHeaders());
        } catch (IOException e){
            throw new RuntimeException();
        }
        byte[] bytesData = crypt.encrypt(streamData.toByteArray());
        byte[] bytesHeaders = crypt.encrypt(streamHeader.toByteArray());

        return ByteBufPayload.create(bytesData, bytesHeaders);
    }

    @Override
    public Message decode(Payload payload) {
        try {

            byte[] bytesData = crypt.decrypt(payload.getData().array());
            byte[] bytesHeaders = crypt.decrypt(payload.getMetadata().array());
            ByteArrayInputStream streamData = new ByteArrayInputStream(bytesData);
            Hessian2StreamingInput hessianData = new Hessian2StreamingInput(streamData);

            ByteArrayInputStream streamHeader = new ByteArrayInputStream(bytesHeaders);
            Hessian2StreamingInput hessianHeader = new Hessian2StreamingInput(streamHeader);

            try {
                // noinspection unchecked
                Message message = new Message((HashMap<String, String>)hessianHeader.readObject(), hessianData.readObject());
                String c = message.getHeader("Crypt");
                if (crypt.cryptSign().equals(c)){
                    return message;
                } else {
                    throw new RuntimeException("Crypt Error! Excepted " + crypt.cryptSign() + ", get " + c );
                }

            } catch (IOException e){
                throw new RuntimeException();
            }
        } catch (BadPaddingException e){
            throw new RuntimeException("Crypt Error! Invalid Key");

        }


    }
}
