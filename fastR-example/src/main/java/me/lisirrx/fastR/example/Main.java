package me.lisirrx.fastR.example;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.client.DefaultClientLoadBalanceStrategy;
import me.lisirrx.fastR.client.RemoteServiceBuilder;
import me.lisirrx.fastR.client.ServiceDiscovery;
import me.lisirrx.fastR.serialization.HessianCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/13
 */
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[]args) throws InterruptedException{

        DefaultClientLoadBalanceStrategy defaultClientLoadBalanceStrategy = new DefaultClientLoadBalanceStrategy();
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(defaultClientLoadBalanceStrategy);
        RemoteServiceBuilder.init(serviceDiscovery, new HessianCodec(), new Address("localhost", 8000));

//        RemoteServiceBuilder.initService(TestService.class, new Address("localhost", 7000));
        TestService testService = RemoteServiceBuilder.ofService(TestService.class);


        People people = new People("li");

        testService.hi(people).log().subscribe(s -> System.out.println("Result : ========" + s.getName()));

        Thread.sleep(5000);


//        testService.many()
//                .subscribe(s -> logger.debug(s));
//
//        Thread.sleep(10000);
    }
}
