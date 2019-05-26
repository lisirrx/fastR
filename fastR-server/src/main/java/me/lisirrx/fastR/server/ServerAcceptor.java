package me.lisirrx.fastR.server;

import io.rsocket.*;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.Codec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/21
 */
public class ServerAcceptor implements SocketAcceptor {

    private ServiceRegistry registry;
    private Codec codec;

    public ServerAcceptor(ServiceRegistry registry, Codec codec) {
        this.registry = registry;
        this.codec = codec;
    }

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {

        return Mono.just(new ServerRSocket());
    }


    private class ServerRSocket extends AbstractRSocket {

        @Override
        public Mono<Void> fireAndForget(Payload payload) {
            Message message = codec.decode(payload);
            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));
            return Mono.defer(
                    () -> Mono.from(
                            invoker.invoke(message.getData())
                    )
            ).map(rep ->
                    codec.encode(new Message(null, rep))
            ).then();
        }

        @Override
        public Mono<Payload> requestResponse(Payload payload) {

            Message message = codec.decode(payload);
            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));

            return Mono.defer(
                    () -> Mono.from(
                            invoker.invoke(message.getData())
                    )
            ).map(rep ->
                    codec.encode(new Message(null, rep))
            );
        }

        @Override
        public Flux<Payload> requestStream(Payload payload) {
            Message message = codec.decode(payload);
            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));
            return Flux.defer(
                    () -> invoker.invoke(message.getData())

            ).map(rep ->
                    codec.encode(new Message(null, rep))
            );
        }

//        @Override
//        public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
//            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));
//            return Flux.from(payloads)
//                    .map(payload -> codec.decode(payload))
//
//            return Flux.defer(
//                    ()-> invoker.invoke(message)
//
//            ).map(rep->
//                    codec.encode(new Message(null, rep))
//            );
//        }

    }
}
