package me.lisirrx.fastR.serialization;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.Hessian2StreamingOutput;
import io.rsocket.Payload;
import io.rsocket.util.ByteBufPayload;
import me.lisirrx.fastR.api.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */
public class HessianCodec implements Codec {


    @Override
    public Payload encode(Message message) {
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
        byte[] bytesData = streamData.toByteArray();
        byte[] bytesHeaders = streamHeader.toByteArray();
        return ByteBufPayload.create(bytesData, bytesHeaders);
    }

    @Override
    public Message decode(Payload payload) {
        byte[] bytesData = payload.getData().array();
        byte[] bytesHeaders = payload.getMetadata().array();

        ByteArrayInputStream streamData = new ByteArrayInputStream(bytesData);
        Hessian2StreamingInput hessianData = new Hessian2StreamingInput(streamData);

        ByteArrayInputStream streamHeader = new ByteArrayInputStream(bytesHeaders);
        Hessian2StreamingInput hessianHeader = new Hessian2StreamingInput(streamHeader);

        try {
            // noinspection unchecked
            return new Message((HashMap<String, String>)hessianHeader.readObject(), hessianData.readObject());

        } catch (IOException e){
            throw new RuntimeException();
        }

    }
}
