package me.lisirrx.fastR.demo.consumer;

import me.lisirrx.fastR.demo.api.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/6/2
 */
@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;

    @GetMapping(value = "/1")
    public Mono<String> get1(){
        return demoService.demo();
    }

    @GetMapping(value = "/",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> get(){
        return demoService.demos().log();
    }

    @GetMapping(value = "/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(Object::toString).log();
    }


}
