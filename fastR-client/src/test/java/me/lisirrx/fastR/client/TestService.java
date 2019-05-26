package me.lisirrx.fastR.client;

import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/24
 */
public interface TestService {

    Mono<Void> empty();
}
