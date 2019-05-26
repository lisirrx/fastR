package me.lisirrx.fastR.client;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/24
 */
public class MethodInfoTest {

    @Test
    public void test(){
        MethodInfo methodInfo = new MethodInfo(TestService.class.getMethods()[0], TestService.class);
    }
}
