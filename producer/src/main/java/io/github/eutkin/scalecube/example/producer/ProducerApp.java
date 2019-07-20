package io.github.eutkin.scalecube.example.producer;

import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;

public class ProducerApp {

  public static void main(String[] args) {
    ProducerService producerService = new ProducerService();
    Microservices microservices =
    Microservices.builder()
        .discovery(
            endpoint ->
                new ScalecubeServiceDiscovery(endpoint)
                    .options(
                        dc ->
                            dc.transport(tc -> tc.port(10000))
                                .membership(mc -> mc.seedMembers(Address.from("localhost:10001")))))
        .transport(RSocketServiceTransport::new)
        .services(producerService)
        .startAwait();

    producerService
            .produce()
            .subscribe(System.out::println);

    microservices.onShutdown().block();
  }
}
