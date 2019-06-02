package me.lisirrx.fastR.center;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.register.*;
import me.lisirrx.fastR.server.FastRSocketService;
import me.lisirrx.fastR.server.ServiceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */

public class DefaultDiscoveryService implements DiscoveryService, RegisterService{

    private ConcurrentHashMap<String, Set<Address>> services = new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(DefaultDiscoveryService.class);

    @Override
    @ServiceMethod
    public Mono<RegisterResponse> register(RegisterRequest request) {
        logger.info("Service Register : [" + request.getServiceName() + "]" + " from (" + request.getAddress() + ")");
        synchronized (this){
            String serviceName = request.getServiceName();
            if (services.containsKey(serviceName)){
                services.get(serviceName).add(request.getAddress());
            } else {
                Set<Address> set = new HashSet<>();
                set.add(request.getAddress());
                services.put(request.getServiceName(), set);
            }
            return Mono.just(RegisterResponse.ok());
        }

    }

    @Override
    @ServiceMethod
    public Mono<ServiceDiscoveryResponse> discover(ServiceDiscoveryRequest request) {

        ArrayList<Address> addresses = new ArrayList<>(services.get(request.getServiceName()));


        return Mono.just(new ServiceDiscoveryResponse(addresses));
    }
}
