package io.github.eutkin.scalecube.example;

import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;

public class ConsumerApp {

  public static void main(String[] args) {
    ConsumerService consumerService = new ConsumerService();
    Microservices.builder()
        .discovery(
            endpoint ->
                new ScalecubeServiceDiscovery(endpoint)
                    .options(dc -> dc.transport(tc -> tc.port(10001))))
        .transport(RSocketServiceTransport::new)
        .services(consumerService)
        .startAwait()
        .onShutdown()
        .block();
  }
}
