package me.lisirrx.fastR.server;

import io.rsocket.*;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.Codec;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/21
 */
public class ServerAcceptor implements SocketAcceptor {

    private ServiceRegistry registry;
    private Codec codec;

    private Logger logger = LoggerFactory.getLogger(ServerAcceptor.class);

    public ServerAcceptor(ServiceRegistry registry, Codec codec) {
        this.registry = registry;
        this.codec = codec;
    }

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {

        return Mono.just(new ServerRSocket());
    }


    private class ServerRSocket extends AbstractRSocket {

        private Mono<Payload> handle(Message message, Invoker invoker){
            return  Mono.defer(
                    () -> Mono.from(
                            invoker.invoke(message.getData())
                    )
            ).map(rep ->
                    codec.encode(new Message(rep))
            ).doOnError(throwable -> logger.error(throwable.getMessage()));
        }

        @Override
        public Mono<Void> fireAndForget(Payload payload) {
            Message message = codec.decode(payload);
            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));
            return handle(message, invoker).then();
        }

        @Override
        public Mono<Payload> requestResponse(Payload payload) {

            Message message = codec.decode(payload);
            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));

            return handle(message, invoker);
        }

        @Override
        public Flux<Payload> requestStream(Payload payload) {
            Message message = codec.decode(payload);
            Invoker invoker = registry.getInvoker(message.getHeader(Message.IDENTITY));
            return Flux.defer(
                    () -> invoker.invoke(message.getData())

            ).map(rep ->
                    codec.encode(new Message(rep))
            ).doOnError(throwable -> logger.error(throwable.getMessage()));
        }

        @Override
        public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
            return Flux.from(payloads)
                    .map(payload -> codec.decode(payload))
                    .map(message -> registry.getInvoker(message.getHeader(Message.IDENTITY)).invoke(message.getData()))
                    .map(req->codec.encode(new Message(req)))
                    .doOnError(throwable -> logger.error(throwable.getMessage()));

        }

    }
}
