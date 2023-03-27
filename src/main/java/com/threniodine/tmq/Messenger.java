package com.threniodine.tmq;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Data
public class Messenger {
    private Sinks.Many<GameState> sink = Sinks.many().multicast().onBackpressureBuffer();
    private Flux<GameState> flux = sink.asFlux();
}
