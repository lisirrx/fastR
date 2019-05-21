package me.lisirrx.fastR.serialization;

import io.rsocket.Payload;
import me.lisirrx.fastR.api.Message;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/13
 */
public interface Codec {

    Payload encode(Message message);

    Message decode(Payload payload);
}
