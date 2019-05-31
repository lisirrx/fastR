package me.lisirrx.fastR.demo.consumer;

import me.lisirrx.fastR.demo.api.DemoService;
import me.lisirrx.fastR.server.FastRSocketService;
import me.lisirrx.fastR.server.ServiceMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/31
 */
@Service
@FastRSocketService
public class DemoServiceImpl implements DemoService {
    @Override
    @ServiceMethod
    public Mono<String> demo() {
        return Mono.just("Demo!");
    }
}
