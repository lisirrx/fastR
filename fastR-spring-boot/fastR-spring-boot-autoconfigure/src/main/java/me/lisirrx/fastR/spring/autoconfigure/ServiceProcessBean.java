package me.lisirrx.fastR.spring.autoconfigure;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.register.RegisterService;
import me.lisirrx.fastR.client.DefaultClientLoadBalanceStrategy;
import me.lisirrx.fastR.client.RemoteServiceBuilder;
import me.lisirrx.fastR.client.ServiceDiscovery;
import me.lisirrx.fastR.serialization.HessianCodec;
import me.lisirrx.fastR.server.FastRServer;
import me.lisirrx.fastR.server.FastRSocketService;
import me.lisirrx.fastR.server.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/31
 */
@Configuration
@Component
public class ServiceProcessBean implements ApplicationContextAware {

    @Value("${fastrsocket.port:7000}")
    private int port;

    @Value("${fastrsocket.host:127.0.0.1}")
    private String host;

    @Value("${fastrsocket.center.host:127.0.0.1}")
    private String centerHost;

    @Value("${fastrsocket.center.port:7000}")
    private int centerPort;

    @Value("${fastrsocket.center.on:false}")
    private boolean centerOn;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Address centerAddress = new Address(centerHost, centerPort);
        Address address = new Address(host, port);

        RegisterService registerService = RemoteServiceBuilder.initService(RegisterService.class, centerAddress);
        ServiceRegistry registry = new ServiceRegistry(address, centerOn);
        registry.setRegisterService(registerService);


        applicationContext.getBeansWithAnnotation(FastRSocketService.class).forEach((s, o) -> {
            registry.register(o);
        });

        new FastRServer.Builder()
                .address(address)
                .registry(registry)
                .codec(new HessianCodec())
                .build()
                .start();
    }
}
