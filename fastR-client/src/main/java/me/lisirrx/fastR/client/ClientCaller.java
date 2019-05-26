package me.lisirrx.fastR.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.Codec;
import org.reactivestreams.Publisher;
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

    public Mono<Void> fireAndForget(Message message){
        return socket.flatMap(
                rSocket ->
                        rSocket.fireAndForget(codec.encode(message))
        );
    }
    public Mono<Message> requestAndResponse(Message message){
        return socket.flatMap(
                rSocket ->
                        rSocket.requestResponse(codec.encode(message))
        )
                .map(
                        payload -> codec.decode(payload)
                );
    }
    public Flux<Message> requestStream(Message message){
        return socket.flux().flatMap(
                rSocket -> rSocket.requestStream(codec.encode(message))
        )
                .map(
                        payload -> codec.decode(payload)
                );
    }

    public Flux<Message> requestChannel(Publisher<Payload> message){
        return socket.flux().flatMap(
                rSocket -> rSocket.requestChannel(message)
        )
                .map(
                        payload -> codec.decode(payload)
                );
    }


}
