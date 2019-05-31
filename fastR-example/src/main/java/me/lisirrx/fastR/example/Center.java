package me.lisirrx.fastR.example;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.serialization.HessianCodec;
import me.lisirrx.fastR.server.FastRServer;
import me.lisirrx.fastR.server.ServiceRegistry;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public class Center {
    public static void main(String[] args) {


        Address address = new Address("localhost", 7000);

        ServiceRegistry registry = new ServiceRegistry(address, true);
        registry.register(new DefaultDiscoveryService());


        new FastRServer.Builder()
                .address(address)
                .registry(registry)
                .codec(new HessianCodec())
                .build()
                .start();
    }
}
