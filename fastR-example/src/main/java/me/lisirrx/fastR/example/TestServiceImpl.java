package me.lisirrx.fastR.example;


import me.lisirrx.fastR.server.ServiceMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

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

    @Override
    @ServiceMethod
    public Flux<String> many() {

        String [] str = {"00001", "00002", "00003", "00004"};

        return Flux.interval(Duration.ofSeconds(3))
                .map(n -> str[n.intValue()])
                .take(4);
    }

    @Override
    @ServiceMethod
    public Mono<Void> emp() {
        System.out.println("+++++++VOID");
        return Mono.empty().then();
    }
}
