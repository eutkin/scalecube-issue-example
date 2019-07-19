package io.github.eutkin.scalecube.example.producer;

import io.github.eutkin.scalecube.example.InvalidConsumer;
import io.github.eutkin.scalecube.example.Producer;
import io.github.eutkin.scalecube.example.ValidConsumer;
import io.scalecube.services.annotations.Inject;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.Scanner;

public class ProducerService implements Producer {

  @Inject private ValidConsumer validConsumer;

  @Inject private InvalidConsumer invalidConsumer;

  private Flux<String> flux = inFlux();

  @Override
  public Flux<String> validProduce() {
      return flux
                .flatMap(validConsumer::consume);
  }

    @Override
    public Flux<String> invalidProduce() {
        return flux
                .map(Collections::singletonList)
                .flatMap(invalidConsumer::consume);
    }

    private Flux<String> inFlux() {
        Scanner scanner = new Scanner(System.in);
        return Flux.<String>create(
                sink -> {
                    while (scanner.hasNext()) {
                        sink.next(scanner.next());
                    }
                    sink.complete();
                })
                .subscribeOn(Schedulers.newSingle("io"))
                .doOnTerminate(scanner::close);
    }
}
