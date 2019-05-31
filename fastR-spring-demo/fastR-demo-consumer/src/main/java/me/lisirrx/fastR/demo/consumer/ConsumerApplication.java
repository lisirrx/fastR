package me.lisirrx.fastR.demo.consumer;

import me.lisirrx.fastR.client.RemoteServiceBuilder;
import me.lisirrx.fastR.demo.api.DemoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/31
 */
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args){
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public DemoService demoService(){
        return RemoteServiceBuilder.ofService(DemoService.class);
    }

    @Bean
    public CommandLineRunner runner(DemoService demoService){
        return new CommandLineRunner() {
            public void run(String... args){
                demoService.demo().subscribe(System.out::println);
            }
        };
    }

}
