package me.lisirrx.fastR.example;

import me.lisirrx.fastR.serialization.HessianCodec;
import me.lisirrx.fastR.server.FastRServer;
import me.lisirrx.fastR.server.ServiceMethod;
import me.lisirrx.fastR.server.ServiceRegistry;
import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/17
 */
public class Server {



    public static void main(String[] args) {
//        EventLoopGroup eventExecutors =  new NioEventLoopGroup(4);
//
//        TcpServer server = TcpServer.create().runOn(eventExecutors).host("localhost").port(7000);
//        RSocketFactory.receive()
//                .acceptor(
//                        (setupPayload, reactiveSocket) ->
//                                Mono.just(
//                                        new AbstractRSocket() {
//
//                                            @Override
//                                            public Mono<Payload> requestResponse(Payload p) {
//                                                System.out.println("+++++" + p.getData().toString());
//                                                HessianCodec codec = new HessianCodec();
//
//                                                Message message = new Message("hello");
//
//                                                return Mono.just(DefaultPayload.create(codec.encode(message)));
//
////                                                return Mono.just(DefaultPayload.create("hello"));
//
//                                            }
//                                        }))
//                .transport(TcpServerTransport.create(server))
//                .start()
//                .publishOn(Schedulers.elastic())
//                .block().onClose().block();
//
//
//        System.out.print(123);


        ServiceRegistry registry = new ServiceRegistry();
        registry.register(new TestServiceImpl());


        new FastRServer.Builder()
                .address("localhost", 7000)
                .registry(registry)
                .codec(new HessianCodec())
                .build()
                .start();
        }
}
