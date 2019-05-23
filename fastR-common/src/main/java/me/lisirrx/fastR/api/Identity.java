package me.lisirrx.fastR.api;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/23
 */
public class Identity {
    private static final String SEPRATOR = "#";

    public static String generate(Class<?> service, Method method){
        return service.getName()
                + SEPRATOR
                + method.getName()
                + "("
                + String.join(",", Arrays.stream(method.getParameterTypes())
                .map(Class::getName).collect(Collectors.toList()));
    }
}
