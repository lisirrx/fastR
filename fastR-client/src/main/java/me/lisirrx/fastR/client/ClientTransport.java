package me.lisirrx.fastR.client;

import io.netty.channel.nio.NioEventLoopGroup;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.serialization.Codec;
import reactor.core.publisher.Mono;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/18
 */
public class ClientTransport {

    private ConcurrentHashMap<Address, Mono<RSocket>> rSockets = new ConcurrentHashMap<>();

    public ClientCaller connect(Address address, Codec codec) {
        Mono<RSocket> rSocket = rSockets.computeIfAbsent(address, (targetAddress)->{

//            NioEventLoopGroup eventExecutors = new NioEventLoopGroup(16);
            LoopResources loopResources = LoopResources.create("RSocket-worker");
            TcpClient tcpClient = TcpClient.newConnection()
                    .runOn(loopResources)
                    .host(address.getHost())
                    .port(address.getPort());

            return RSocketFactory.connect()
                    .transport(()->TcpClientTransport.create(tcpClient))
                    .start().cache();
        });

        return new ClientCaller(rSocket, codec);
    }
}

