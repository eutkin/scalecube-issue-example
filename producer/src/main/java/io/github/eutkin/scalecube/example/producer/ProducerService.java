package io.github.eutkin.scalecube.example.producer;

import io.github.eutkin.scalecube.example.Consumer;
import io.github.eutkin.scalecube.example.Producer;
import io.scalecube.services.annotations.Inject;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Scanner;

public class ProducerService implements Producer {

  @Inject private Consumer consumer;

  @Override
  public Flux<String> produce() {
    return inFlux().flatMap(consumer::consume);
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
