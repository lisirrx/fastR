package me.lisirrx.fastR.server;

import me.lisirrx.fastR.api.Message;
import org.reactivestreams.Publisher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/21
 */
public class Invoker {
    private Method method;
    private Object service;

    public Publisher<?> invoke(Object data){

        try {
            if (method.getParameterCount() == 0){
                return (Publisher<?>) method.invoke(service);
            }
            return (Publisher<?>) method.invoke(service, data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setService(Object service) {
        this.service = service;
    }
}
