package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */
public class ServiceDiscovery {

    private ConcurrentHashMap<String, List<Address>> discovery;

    public ServiceDiscovery(){
        discovery = new ConcurrentHashMap<>();
    }

    public void put(ServiceInfo serviceInfo, Address address){
        discovery.put(serviceInfo.getServiceName(), List.of(address));
    }

    public Address lookup(ServiceInfo serviceInfo){
        // TODO load balance
        return discovery.computeIfAbsent(serviceInfo.getServiceName(), name->{
            // TODO remote
            return List.of(new Address("localhost", 8000));
        }).get(0);
    }
}
