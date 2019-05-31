package me.lisirrx.fastR.server;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.Identity;
import me.lisirrx.fastR.api.register.DiscoveryService;
import me.lisirrx.fastR.api.register.RegisterRequest;
import me.lisirrx.fastR.api.register.RegisterResponse;
import me.lisirrx.fastR.api.register.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/21
 */
public class ServiceRegistry {

    private ConcurrentHashMap<String, Invoker> invokers = new ConcurrentHashMap<>();
    private Address selfAddress;
    private boolean center;
    private RegisterService registerService;

    private Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    public ServiceRegistry(Address selfAddress, boolean center) {
        this.selfAddress = selfAddress;
        this.center = center;
    }

    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }

    public void register(Object serviceImpl){
        Arrays.stream(serviceImpl.getClass().getInterfaces())
                .forEach(clazz->{
                    Arrays.stream(serviceImpl.getClass().getMethods())
                            .forEach(method -> {
                                ServiceMethod annotation = method.getAnnotation(ServiceMethod.class);

                                if (annotation != null){
                                    Invoker invoker = new Invoker();
                                    invoker.setMethod(method);
                                    invoker.setService(serviceImpl);
                                    invokers.put(Identity.generate(clazz, method), invoker);
                                    logger.info("Register Service" + clazz.getName());
                                    if (!center){
                                        RegisterRequest registerRequest = new RegisterRequest(clazz.getName(), selfAddress);
                                        logger.info(registerService.register(registerRequest).map(RegisterResponse::getMsg).block());
                                    }

                                }
                            });
                });

    }

    private boolean containMethod(Class<?> clazz, Method method){
        return Arrays.stream(clazz.getMethods()).anyMatch(
                method1 -> method1.equals(method)
        );
    }

    public Invoker getInvoker(String identity){
        return invokers.get(identity);
    }
}
