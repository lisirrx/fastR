package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.register.DiscoveryService;
import me.lisirrx.fastR.serialization.Codec;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/15
 */
public class RemoteServiceBuilder {

    private static ConcurrentHashMap<Class<?>, ? super Object> services = new ConcurrentHashMap<>();
    private static ServiceDiscovery serviceDiscovery;
    private static Codec codec;
    private static ClientTransport clientTransport = new ClientTransport();

    public static <T> T ofService(Class<T> service){

        ServiceInfo serviceInfo = new ServiceInfo(service);

        // noinspection unchecked
        return (T)
               services.computeIfAbsent(service,
                       (aClazz)-> Proxy.newProxyInstance(service.getClassLoader(),
                               new Class[]{service},
                               new ServiceInvocationHandler(clientTransport, serviceDiscovery, serviceInfo, codec)));

    }

    public static void init(ServiceDiscovery discovery, Codec aCodec, Address discoveryAddress){
        serviceDiscovery = discovery;
        codec = aCodec;
        DiscoveryService service = initService(DiscoveryService.class, discoveryAddress);
        discovery.setDiscoveryService(service);
    }

    public static <T> T initService(Class<T> service, Address address){
        ServiceInfo serviceInfo = new ServiceInfo(service);
        serviceDiscovery.setService(serviceInfo, address);
        return ofService(service);
    }
}
