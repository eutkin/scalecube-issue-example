package io.github.eutkin.scalecube.example;

import reactor.core.publisher.Mono;

public class ConsumerService implements Consumer {
    @Override
    public Mono<String> consume(String input) {
        return Mono.just(input.toUpperCase());
    }
}
