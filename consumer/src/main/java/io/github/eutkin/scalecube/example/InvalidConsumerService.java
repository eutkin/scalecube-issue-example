package io.github.eutkin.scalecube.example;

import reactor.core.publisher.Mono;

import java.util.List;

public class InvalidConsumerService implements InvalidConsumer {
    @Override
    public Mono<String> consume(List<String> input) {
        return Mono.just(input.toString());
    }
}
