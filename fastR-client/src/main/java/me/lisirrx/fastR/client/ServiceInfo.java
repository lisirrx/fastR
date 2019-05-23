package me.lisirrx.fastR.client;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */
public class ServiceInfo {
    private Map<Method, MethodInfo> methods;

    private String serviceName;

    public ServiceInfo(){
        this.methods = new HashMap<>();
    }

    public ServiceInfo(Class service){
        this.serviceName = service.getName();
        this.methods = Arrays.stream(service.getMethods())
                .collect(Collectors.toMap(Function.identity(), method -> new MethodInfo(method, service)));
    }

    public MethodInfo getMethod(Method method){
        return methods.get(method);
    }

    public String getServiceName() {
        return serviceName;
    }

}
