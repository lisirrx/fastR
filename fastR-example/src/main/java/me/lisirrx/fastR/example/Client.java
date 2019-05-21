package me.lisirrx.fastR.example;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

import java.time.Duration;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/20
 */
public class Client {
    public static void main(String[] args) throws InterruptedException{

        TcpClient client = TcpClient.create().host("localhost").port(7000);
        RSocket rSocket = RSocketFactory.connect()
                .transport(TcpClientTransport.create(client))
                .start()
                .block();

        rSocket.requestResponse(DefaultPayload.create("HI"))
                .doOnError(throwable -> System.out.print("========:" + throwable))
                .subscribe(
                        payload -> System.out.println("==============》" + payload.getData().toString()));
//        rSocket.dispose();

        Thread.sleep(5000);
            System.out.print(123);
    }
}
