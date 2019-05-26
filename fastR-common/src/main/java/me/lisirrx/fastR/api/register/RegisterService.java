package me.lisirrx.fastR.api.register;

import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public interface RegisterService {
    Mono<RegisterResponse> register(RegisterRequest request);
}
