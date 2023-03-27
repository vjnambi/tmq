package com.threniodine.tmq;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Data
public class Messenger {
    Sinks.Many<Game> sink = Sinks.many().multicast().onBackpressureBuffer();
    Flux<Game> flux = sink.asFlux();
}
