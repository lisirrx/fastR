package me.lisirrx.fastR.client;

import io.rsocket.RSocket;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.Codec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Flow;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/18
 */
public class ClientCaller {

    private Mono<RSocket> socket;
    private Codec codec;

    public ClientCaller(Mono<RSocket> socket, Codec codec) {
        this.socket = socket;
        this.codec = codec;
    }

    public Mono<Message> fireAndForget(Message message){
        return null;
    }
    public Mono<Message> requestAndResponse(Message message){
        return socket.flatMap(
                rSocket ->
                        rSocket.requestResponse(codec.encode(message))
        )
                .flatMap(
                        payload -> Mono.just(codec.decode(payload))
                );
    }
    public Flux<Message> requestStream(Message message){
        return null;
    }

    public Flux<Message> requestChannel(Flow.Publisher<Message> message){
        return null;
    }


}
