package io.github.eutkin.scalecube.example;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Flux;

@Service
public interface Producer {

  @ServiceMethod
  Flux<String> validProduce();

  @ServiceMethod
  Flux<String> invalidProduce();
}
