# 5 reasons why Scalecube looks like Dark Souls

```
Disclaimer

This article is not intended to offend or hurt anyone else's feelings. 
```


Hello, my friend. Today, I'll tell you about the dangers that await you when you decide to write a 
simple distributed system using Scalecube Services.

Let's take a simple project. We have `Producer`, who provides the text entered in the console as stream of events
(`Flux<String>`).  We have `Consumer`, who consumes events from `Producer` and transforms text from event to upper case.

## Consumer

Creates interface for *Consumer as Scalecube Service*:

```java
@Service
public interface Consumer {

    @ServiceMethod
    Mono<String> consume(String input);
}
```

Now, we create java service with entrypoint for *Consumer*: 

```java
public class ConsumerApp {

  public static void main(String[] args) {
    ConsumerService consumerService = new ConsumerService();
    Microservices   
        .builder()
        .discovery(endpoint ->
                        new ScalecubeServiceDiscovery(endpoint)
                            .options(dc -> dc.transport(tc -> tc.port(10001)))
        )       
        .services(consumerService)
        .startAwait();
      runAwaitThread();
  }
}
```
We have specified our `Consumer` as *Scalecube Service*, we have added Service Discovery. Is everything okay?

![you-died](https://boygeniusreport.files.wordpress.com/2016/04/dark_souls_you_died.jpg?quality=98&strip=all&w=360)

We didn't specify the transport and so our `Consumer` will be forever alone. Try else:

```java
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
        .startAwait();
      runAwaitThread();
  }
}  
```

This case can only be detected in the early hours when we receive messages that our `Consumer` is not available.

## Producer

Creates interface for *Producer as Scalecube Service*:

```java
@Service
public interface Producer {

  @ServiceMethod
  Flux<String> produce();

}
```

And, learned by painful experience, we create java service with entrypoint for *Producer*: 

```java
public class ProducerApp {

  public static void main(String[] args) {
    ProducerService producerService = new ProducerService();
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
  }
}
```

## Try run it!

Let run out distributed system.

Consumer: 

```
2019-07-19 19:46:39,222 [main] INFO  io.scalecube.services.Microservices - Starting microservices 68594cab-2869-456f-a176-6fae0aeb97d2
2019-07-19 19:46:40,388 [boss-transport-3-1] INFO  io.scalecube.services.Microservices - Successfully bound server service transport -- io.scalecube.services.transport.rsocket.RSocketServerTransport@7d7575ab on address 10.0.75.1:65246
2019-07-19 19:46:40,397 [boss-transport-3-1] INFO  io.scalecube.services.Microservices - Successfully created client service transport -- io.scalecube.services.transport.rsocket.RSocketClientTransport@6e33ae4a
2019-07-19 19:46:40,532 [microservices9b068d9d-1] INFO  io.scalecube.services.Microservices - Starting service discovery -- io.scalecube.services.discovery.ScalecubeServiceDiscovery@517e87c0
2019-07-19 19:46:40,594 [sc-cluster-io-nio-1] INFO  io.scalecube.services.Microservices - Successfully started service discovery -- io.scalecube.services.discovery.ScalecubeServiceDiscovery@517e87c0
2019-07-19 19:47:40,900 [sc-cluster-10001-2] INFO  io.scalecube.services.discovery.ServiceDiscovery - Service endpoint d44593f0-4a1b-4941-b78e-7cccffa524de is about to be added, since member 4BD8B96AFB57CD7D362E@10.0.75.1:10000 has joined the cluster
2019-07-19 19:47:40,901 [sc-cluster-10001-2] INFO  io.scalecube.services.registry.ServiceRegistryImpl - ServiceEndpoint registered: ServiceEndpoint{id='d44593f0-4a1b-4941-b78e-7cccffa524de', address='10.0.75.1:65223', tags={}, serviceRegistrations=[ServiceRegistration{namespace='io.github.eutkin.scalecube.example.Producer', tags={}, methods=[ServiceMethodDefinition{action='produce', tags={}, auth=false}]}]}

```

Producer:

```
2019-07-19 19:49:20,703 [main] INFO  io.scalecube.services.Microservices - Starting microservices 17acfb59-e40e-4726-892b-317df579fb26
2019-07-19 19:49:21,705 [boss-transport-3-1] INFO  io.scalecube.services.Microservices - Successfully bound server service transport -- io.scalecube.services.transport.rsocket.RSocketServerTransport@652383d5 on address 10.0.75.1:65319
2019-07-19 19:49:21,713 [boss-transport-3-1] INFO  io.scalecube.services.Microservices - Successfully created client service transport -- io.scalecube.services.transport.rsocket.RSocketClientTransport@4fa4006b
2019-07-19 19:49:21,854 [microservices85258ed6-1] INFO  io.scalecube.services.Microservices - Starting service discovery -- io.scalecube.services.discovery.ScalecubeServiceDiscovery@352917ad
2019-07-19 19:49:22,515 [sc-cluster-10000-2] INFO  io.scalecube.services.discovery.ServiceDiscovery - Service endpoint 9a765428-7f62-4d6b-8438-b00c886216d8 is about to be added, since member 99D506399B3B25BC9F9E@10.0.75.1:10001 has joined the cluster
2019-07-19 19:49:22,516 [sc-cluster-10000-2] INFO  io.scalecube.services.registry.ServiceRegistryImpl - ServiceEndpoint registered: ServiceEndpoint{id='9a765428-7f62-4d6b-8438-b00c886216d8', address='10.0.75.1:65299', tags={}, serviceRegistrations=[ServiceRegistration{namespace='io.github.eutkin.scalecube.example.Consumer', tags={}, methods=[ServiceMethodDefinition{action='consume', tags={}, auth=false}]}]}
2019-07-19 19:49:22,519 [sc-cluster-10000-2] INFO  io.scalecube.services.Microservices - Successfully started service discovery -- io.scalecube.services.discovery.ScalecubeServiceDiscovery@352917ad
> hi
2019-07-19 19:49:25,971 [rsocket-worker-1-1] INFO  io.scalecube.services.transport.rsocket.RSocketClientTransport - Connected successfully on 10.0.75.1:65299
< HI
```

It's alive. Now, let restart *Consumer* and try again typing *hi* in *Producer* console.

```
> hi
2019-07-19 19:51:34,972 [io-3] ERROR io.scalecube.services.ServiceCall - Failed  to invoke service, 
No reachable member with such service definition [/io.github.eutkin.scalecube.example.Consumer/consume], 
args [ServiceMessage {headers: {q=/io.github.eutkin.scalecube.example.Consumer/consume}, data: hi}]
``` 

It's okay, because our *Consumer* restarting in this moment. Wait few minutes:

```
2019-07-19 19:54:52,819 [sc-cluster-10000-2] INFO  io.scalecube.services.discovery.ServiceDiscovery - Service endpoint 31a14b35-1294-456f-85f2-8a33403189eb is about to be added, since member 0D332CAE6636FCF9CE59@10.0.75.1:10001 has joined the cluster
2019-07-19 19:54:52,819 [sc-cluster-10000-2] INFO  io.scalecube.services.registry.ServiceRegistryImpl - ServiceEndpoint registered: ServiceEndpoint{id='31a14b35-1294-456f-85f2-8a33403189eb', address='10.0.75.1:65422', tags={}, serviceRegistrations=[ServiceRegistration{namespace='io.github.eutkin.scalecube.example.Consumer', tags={}, methods=[ServiceMethodDefinition{action='consume', tags={}, auth=false}]}]}
```

*Consumer* is back the in cluster. Try typing `hi` again:

```
> hi
```

And....

![you-died](https://boygeniusreport.files.wordpress.com/2016/04/dark_souls_you_died.jpg?quality=98&strip=all&w=360)

*Consumer* doesn't want to answer.

Why? 

![horse](https://cdn1.savepice.ru/uploads/2019/7/19/b6ce98c36af99f6d7fa8737c4d53d12b-full.png)

Mmm okay, let's keep going. 

## Interfaces

Do you want to pass more than one parameter to the service method?

```java
@Service
public interface Consumer {

    @ServiceMethod
    Mono<String> consume(String input, Context context);
}
```

![you-died](https://boygeniusreport.files.wordpress.com/2016/04/dark_souls_you_died.jpg?quality=98&strip=all&w=360)

You only have to pass one parameter. You can pass the second parameter, but only if you have security enabled and the 
second argument is the user.

Do you want to pass collection type? 

```java
@Service
public interface Consumer {

    @ServiceMethod
    Mono<String> consume(List<String> input);
}
```

![you-died](https://boygeniusreport.files.wordpress.com/2016/04/dark_souls_you_died.jpg?quality=98&strip=all&w=360)

The Scalecube thinks this argument is of the Flux or Mono type if it is generic, so it will assume that the argument 
type is a `String`.

You have a few implementations for `Consumer` and you want to register them on Scalecube? 
I think you have understood what will happen next.

![you-died](https://boygeniusreport.files.wordpress.com/2016/04/dark_souls_you_died.jpg?quality=98&strip=all&w=360)

## Total...

Before you write anything on Scalecube, make sure you know the architectural principles of information systems, 
have a couple of degrees in Computer Science and think about each action.

![laser](https://www.meme-arsenal.com/memes/b09912b9ead7faea05838a67903a44e7.jpg)

