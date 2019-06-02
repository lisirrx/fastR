package me.lisirrx.fastR.demo.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/31
 */
public interface DemoService {
    Mono<String> demo();

    Flux<String> demos();
}
