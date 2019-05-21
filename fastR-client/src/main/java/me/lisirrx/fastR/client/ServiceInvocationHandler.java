package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/15
 */
public class ServiceInvocationHandler implements InvocationHandler {
    private ClientTransport clientTransport;
    private ServiceDiscovery serviceDiscovery;
    private ServiceInfo serviceInfo;
    private Codec codec;

    public ServiceInvocationHandler(ClientTransport clientTransport,
                                    ServiceDiscovery serviceDiscovery,
                                    ServiceInfo serviceInfo,
                                    Codec codec) {
        this.clientTransport = clientTransport;
        this.serviceDiscovery = serviceDiscovery;
        this.serviceInfo = serviceInfo;
        this.codec = codec;
    }

    private final Logger logger = LoggerFactory.getLogger(ServiceInvocationHandler.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Address address = serviceDiscovery.lookup(serviceInfo);

        MethodInfo methodInfo = serviceInfo.getMethod(method);

        Object param = null;
        if (args.length > 0){
            param = args[0];
        }

        switch (methodInfo.getMode()){
            case FIRE_AND_FORGET:
                break;

            case REQUEST_RESPONSE:
                return clientTransport.connect(address, codec)
                        .requestAndResponse(buildMessage(param, methodInfo))
                        .map(
                                Message::getData
                        )
                        .doOnError(Throwable::printStackTrace);
            case REQUEST_STREAM:
                break;

            case REQUEST_CHANNEL:
                break;
        }
        return null;
    }


    private Message buildMessage(Object param, MethodInfo method){
        Message message = new Message(param);
        message.setHeader(Message.SERVICE, method.getServiceName());
        message.setHeader(Message.METHOD, method.getMethodName());
        return message;
    }


}
