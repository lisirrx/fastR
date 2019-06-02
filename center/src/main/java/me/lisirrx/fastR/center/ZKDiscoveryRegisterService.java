package me.lisirrx.fastR.center;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.register.*;
import me.lisirrx.fastR.server.FastRSocketService;
import me.lisirrx.fastR.server.ServiceMethod;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/6/2
 */
@FastRSocketService
@Service
public class ZKDiscoveryRegisterService implements RegisterService, DiscoveryService {

    @Autowired
    private ZkClient zkClient;
    private Logger logger = LoggerFactory.getLogger(ZKDiscoveryRegisterService.class);

    @Override
    @ServiceMethod
    public Mono<ServiceDiscoveryResponse> discover(ServiceDiscoveryRequest request) {
        List<Address> addresses = zkClient.getChildren("/fastR/" + request.getServiceName())
                .stream()
                .map(s -> new Address(s.split(":")[0], Integer.valueOf(s.split(":")[1])))
                .collect(Collectors.toList());
        ServiceDiscoveryResponse response = new ServiceDiscoveryResponse(addresses);
        return Mono.just(response);
    }

    @Override
    @ServiceMethod
    public Mono<RegisterResponse> register(RegisterRequest request) {
        logger.info("Service Register : [" + request.getServiceName() + "]" + " from (" + request.getAddress() + ")");

        String serviceName = request.getServiceName();
        Address address = request.getAddress();
        String addressStr = address.getHost() + ":" + address.getPort();
        if (!zkClient.exists("/fastR/" + serviceName)){
            zkClient.create("/fastR/" + serviceName, "", CreateMode.PERSISTENT);
        }
        zkClient.create("/fastR/" + serviceName + "/" + addressStr, "", CreateMode.PERSISTENT);

        return Mono.just(RegisterResponse.ok());
    }
}
