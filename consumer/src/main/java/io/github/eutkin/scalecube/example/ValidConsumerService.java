package io.github.eutkin.scalecube.example;

import reactor.core.publisher.Mono;

public class ValidConsumerService implements ValidConsumer {
    @Override
    public Mono<String> consume(String input) {
        return Mono.just(input.toString());
    }
}
