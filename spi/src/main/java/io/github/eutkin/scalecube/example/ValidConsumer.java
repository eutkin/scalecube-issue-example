package io.github.eutkin.scalecube.example;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Mono;

@Service
public interface ValidConsumer {

    @ServiceMethod
    Mono<String> consume(String input);
}
