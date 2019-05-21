package me.lisirrx.fastR.example;

import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/13
 */
public interface TestService {
      Mono<String> hi(String name);
}
