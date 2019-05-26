package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.api.Message;
import me.lisirrx.fastR.serialization.Codec;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Flow;

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

            Mono<Address> serviceAddress = serviceDiscovery.lookup(serviceInfo);

            MethodInfo methodInfo = serviceInfo.getMethod(method);

            Object param = null;
            if (args != null && args.length > 0){
                param = args[0];
            }

            final Object p = param;

            switch (methodInfo.getMode()){
                case FIRE_AND_FORGET:
                    logger.info(methodInfo.toString());

                    return serviceAddress.flatMap(address->
                        clientTransport.connect(address, codec)
                                .fireAndForget(buildMessage(p, methodInfo))
                    );

                case REQUEST_RESPONSE:
                    return serviceAddress.flatMap(address->
                        clientTransport.connect(address, codec)
                                .requestAndResponse(buildMessage(p, methodInfo))
                                .map(
                                        Message::getData
                                )
                    );

                case REQUEST_STREAM:
                    return serviceAddress.flatMapMany(address->
                            clientTransport.connect(address, codec)
                                    .requestStream(buildMessage(p, methodInfo))
                                    .map(
                                            Message::getData
                                    )
                    );
            //            case REQUEST_CHANNEL:
            //                if (param.getClass().isAssignableFrom(Publisher.class)){
            //                    return clientTransport.connect(address, codec)
            //                            .requestChannel(Flux.just(codec.encode(buildMessage(param, methodInfo))))
            //                            .map(Message::getData)
            //                            .doOnError(Throwable::printStackTrace);
            //
            //                }

            }
            return null;
    }


    private Message buildMessage(Object param, MethodInfo method){
        Message message = new Message(param);
        message.setHeader(Message.SERVICE, method.getServiceName());
        message.setHeader(Message.METHOD, method.getMethodName());
        message.setHeader(Message.IDENTITY, method.getIdentity());
        return message;
    }


}
