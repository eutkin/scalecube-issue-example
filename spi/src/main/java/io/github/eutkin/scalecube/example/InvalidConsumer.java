package io.github.eutkin.scalecube.example;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface InvalidConsumer {

    @ServiceMethod
    Mono<String> consume(List<String> input);
}
