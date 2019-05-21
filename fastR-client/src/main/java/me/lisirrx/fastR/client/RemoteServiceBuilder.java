package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.serialization.HessianCodec;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/15
 */
public class RemoteServiceBuilder {

    private static ConcurrentHashMap<Class<?>, ? super Object> services = new ConcurrentHashMap<>();
    private static ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
    private static HessianCodec hessianCodec = new HessianCodec();
    private static ClientTransport clientTransport = new ClientTransport();

    public static  <T> T ofService(Class<T> service){

        ServiceInfo serviceInfo = new ServiceInfo(service);

        // noinspection unchecked
        return (T)
               services.computeIfAbsent(service,
                       (aClazz)-> Proxy.newProxyInstance(service.getClassLoader(),
                               new Class[]{service},
                               new ServiceInvocationHandler(clientTransport, serviceDiscovery, serviceInfo, hessianCodec)));

    }

    public static void initService(Class<?> service, Address address){
        ServiceInfo serviceInfo = new ServiceInfo(service);
        serviceDiscovery.put(serviceInfo, address);
        ofService(service);
    }
}
