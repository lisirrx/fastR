package me.lisirrx.fastR.example;

import me.lisirrx.fastR.api.Address;
import me.lisirrx.fastR.client.RemoteServiceBuilder;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/13
 */
public class Main {
    public static void main(String[]args) throws InterruptedException{

        RemoteServiceBuilder.initService(TestService.class, new Address("localhost", 7000));
        TestService testService = RemoteServiceBuilder.ofService(TestService.class);


        People people = new People("li");

        testService.hi(people).subscribe(s -> System.out.println("Result : ========" + s.getName()));

        Thread.sleep(3000);
    }
}
