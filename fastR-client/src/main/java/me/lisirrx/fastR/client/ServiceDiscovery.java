package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.register.DiscoveryService;
import me.lisirrx.fastR.api.register.ServiceDiscoveryRequest;
import me.lisirrx.fastR.api.register.ServiceDiscoveryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Fuseable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */
public class ServiceDiscovery {

    private ConcurrentHashMap<String, List<Address>> discovery ;
    private ClientLoadBalanceStrategy loadBalanceStrategy;
    private DiscoveryService discoveryService;

    private Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);


    public ServiceDiscovery(ClientLoadBalanceStrategy loadBalanceStrategy){
        this.discovery = new ConcurrentHashMap<>();
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    public void put(ServiceInfo serviceInfo, Address address){
        checkDiscoveryServiceExist();
        discovery.put(serviceInfo.getServiceName(), List.of(address));
    }

    protected void setService(ServiceInfo serviceInfo, Address address){
        discovery.put(serviceInfo.getServiceName(), List.of(address));

    }
    protected void setDiscoveryService(DiscoveryService service){
        this.discoveryService = service;
    }

    public Mono<Address> lookup(ServiceInfo serviceInfo){
        checkDiscoveryServiceExist();
        logger.debug(discovery.getOrDefault(serviceInfo.getServiceName(), List.of(new Address("======",0))).toString());


        Callable<List<Address>> callable = ()->{
            return discoveryService.discover(new ServiceDiscoveryRequest(serviceInfo.getServiceName()))
                    .map(ServiceDiscoveryResponse::getAddresses).block();
        };

        return Mono.justOrEmpty(
                discovery.get(serviceInfo.getServiceName())
        )
                .switchIfEmpty(
                    Mono.fromCallable(callable))
                .map(addresses -> {
                    discovery.put(serviceInfo.getServiceName(), addresses);
                    return addresses;
                })
                .map(
                    addresses -> loadBalanceStrategy.loadBalance(addresses)
                );
    }

    private void checkDiscoveryServiceExist(){
        if (discoveryService == null){
            throw new RuntimeException("DiscoveryService has not been init");
        }
    }
}
