package io.github.eutkin.scalecube.example;

import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;

public class ConsumerApp {

  public static void main(String[] args) {
    ValidConsumerService consumerService = new ValidConsumerService();
      InvalidConsumerService invalidConsumerService = new InvalidConsumerService();
      Microservices.builder()
            .discovery(
                    endpoint -> new ScalecubeServiceDiscovery(endpoint)
                            .options(dc -> dc
                                    .transport(tc -> tc.port(10001))
                            )
            )
            .transport(RSocketServiceTransport::new)
            .services(consumerService, invalidConsumerService)
            .startAwait();
      Thread thread = new Thread(() -> {
          while (true) {

          }
      });
      thread.setDaemon(false);
      thread.start();
  }
}
