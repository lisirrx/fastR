package me.lisirrx.fastR.server;

import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.serialization.Codec;
import me.lisirrx.fastR.serialization.HessianCodec;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/20
 */
public class FastRServer {
    private Mono<CloseableChannel> channel;


    public FastRServer(Mono<CloseableChannel> channel) {
        this.channel = channel;
    }


    public void start(){
        this.channel.publishOn(Schedulers.elastic()).block().onClose().block();
    }

    public static class Builder {
        private SocketAcceptor acceptor;
        private Address address;
        private ServiceRegistry serviceRegistry;
        private Codec codec;
        private ServerTransport<CloseableChannel> serverTransport;

        public Builder address(String host, int port){
            this.address = new Address(host, port);
            return this;
        }

        public Builder address(Address address){
            this.address = address;
            return this;
        }

        public Builder codec(Codec codec){
            this.codec = codec;
            return this;
        }

        public Builder registry(ServiceRegistry registry){
            this.serviceRegistry = registry;
            return this;
        }

        public Builder transport(ServerTransport<CloseableChannel> serverTransport){
            this.serverTransport = serverTransport;
            return this;
        }

        public FastRServer build(){
            if (serverTransport == null){
                this.serverTransport = TcpServerTransport.create(address.getHost(), address.getPort());
            }

            return new FastRServer(
                    RSocketFactory.receive().acceptor(new ServerAcceptor(this.serviceRegistry, this.codec))
                            .transport(this.serverTransport).start()
            );
        }
    }

}
