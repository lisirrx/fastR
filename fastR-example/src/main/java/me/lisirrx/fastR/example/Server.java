package me.lisirrx.fastR.example;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.register.RegisterService;
import me.lisirrx.fastR.client.DefaultClientLoadBalanceStrategy;
import me.lisirrx.fastR.client.RemoteServiceBuilder;
import me.lisirrx.fastR.client.ServiceDiscovery;
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
        Address address = new Address("localhost", 9000);
        Address centerAddress = new Address("localhost", 8000);

        DefaultClientLoadBalanceStrategy defaultClientLoadBalanceStrategy = new DefaultClientLoadBalanceStrategy();
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(defaultClientLoadBalanceStrategy);
        RemoteServiceBuilder.init(serviceDiscovery, new HessianCodec(), centerAddress);
        RegisterService registerService = RemoteServiceBuilder.initService(RegisterService.class, centerAddress);

        ServiceRegistry registry = new ServiceRegistry(address, false);
        registry.setRegisterService(registerService);
        registry.register(new TestServiceImpl());


        new FastRServer.Builder()
                .address(address)
                .registry(registry)
                .codec(new HessianCodec())
                .build()
                .start();

        System.out.println("hahah");
        }
}
