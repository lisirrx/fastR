package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Identity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

import static me.lisirrx.fastR.client.RSocketMode.REQUEST_CHANNEL;
import static me.lisirrx.fastR.client.RSocketMode.REQUEST_STREAM;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */
public class MethodInfo {
    private RSocketMode mode;
    private String serviceName;
    private String methodName;
    private String identity;

    public MethodInfo(Method method, Class<?> service){
        this.mode = mode(method);
        this.serviceName = service.getName();
        this.methodName = method.getName();
        this.identity = Identity.generate(service, method);
    }

    public RSocketMode getMode() {
        return mode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    private RSocketMode mode(Method method){
        Class<?> returnType = method.getReturnType();
        if (returnType.isAssignableFrom(Void.TYPE)){
            return RSocketMode.FIRE_AND_FORGET;
        } else if (returnType.isAssignableFrom(Mono.class)){
            return RSocketMode.REQUEST_RESPONSE;
        } else if (returnType.isAssignableFrom(Flux.class)){
            Class<?>[] parmTypes = method.getParameterTypes();
            boolean first =
                    parmTypes.length > 0
                            &&
                            (Flux.class.isAssignableFrom(parmTypes[0])
                            || Publisher.class.isAssignableFrom(parmTypes[0]));

            return first ? REQUEST_CHANNEL : REQUEST_STREAM;
        } else {
            throw new IllegalArgumentException(
                    "Service method is not supported (check return type or parameter type): " + method);
        }
    }

    public String getIdentity() {
        return identity;
    }
}
