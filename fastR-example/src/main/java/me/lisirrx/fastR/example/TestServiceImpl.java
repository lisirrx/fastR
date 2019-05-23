package me.lisirrx.fastR.example;


import me.lisirrx.fastR.server.ServiceMethod;
import reactor.core.publisher.Mono;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/23
 */
public class TestServiceImpl implements TestService {
    @Override
    @ServiceMethod
    public Mono<People> hi(People people) {
        return Mono.just(people)
                .map(people1 -> {
                    people1.setName(people1.getName() + "122223");
                    return people1;
                });
    }
}
