package me.lisirrx.fastR.server;

import me.lisirrx.fastR.api.Identity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/21
 */
public class ServiceRegistry {

    private ConcurrentHashMap<String, Invoker> invokers = new ConcurrentHashMap<>();

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
