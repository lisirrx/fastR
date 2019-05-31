package me.lisirrx.fastR.spring.autoconfigure;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.client.DefaultClientLoadBalanceStrategy;
import me.lisirrx.fastR.client.RemoteServiceBuilder;
import me.lisirrx.fastR.client.ServiceDiscovery;
import me.lisirrx.fastR.serialization.HessianCodec;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/31
 */
@Component
@Configuration
public class ServiceInitializer implements ApplicationContextInitializer {

    @Value("${fastrsocket.port:7000}")
    private int port;

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        Integer centerPort = configurableApplicationContext.getEnvironment().getProperty("fastrsocket.center.port", Integer.class, 7000);
        String centerHost = configurableApplicationContext.getEnvironment().getProperty("fastrsocket.center.host", String.class,"127.0.0.1");

        Address centerAddress = new Address(centerHost, centerPort);

        DefaultClientLoadBalanceStrategy defaultClientLoadBalanceStrategy = new DefaultClientLoadBalanceStrategy();
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(defaultClientLoadBalanceStrategy);
        RemoteServiceBuilder.init(serviceDiscovery, new HessianCodec(), centerAddress);
    }
}
