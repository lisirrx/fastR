package me.lisirrx.fastR.example;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.HessianCodec;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.netty.tcp.TcpServer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/17
 */
public class Server {
    public static void main(String[] args) {
        EventLoopGroup eventExecutors =  new NioEventLoopGroup(4);

        TcpServer server = TcpServer.create().runOn(eventExecutors).host("localhost").port(7000);
        RSocketFactory.receive()
                .acceptor(
                        (setupPayload, reactiveSocket) ->
                                Mono.just(
                                        new AbstractRSocket() {

                                            @Override
                                            public Mono<Payload> requestResponse(Payload p) {
                                                System.out.println("+++++" + p.getData().toString());
                                                HessianCodec codec = new HessianCodec();

                                                Message message = new Message("hello");

                                                return Mono.just(DefaultPayload.create(codec.encode(message)));

//                                                return Mono.just(DefaultPayload.create("hello"));

                                            }
                                        }))
                .transport(TcpServerTransport.create(server))
                .start()
                .publishOn(Schedulers.elastic())
                .block().onClose().block();


        System.out.print(123);
    }
}
