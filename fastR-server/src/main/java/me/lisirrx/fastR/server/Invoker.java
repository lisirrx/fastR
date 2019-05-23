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

    public Publisher<?> invoke(Message message){
        System.out.print(message);
        try {
            return (Publisher<?>) method.invoke(service, message.getData());
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
