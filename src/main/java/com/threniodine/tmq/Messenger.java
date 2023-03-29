package com.threniodine.tmq;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Data
public class Messenger {
    private Sinks.Many<GameState> sink;
    private Flux<GameState> flux;

    public Messenger(){
        sink = Sinks.many().multicast().onBackpressureBuffer();
        flux = sink.asFlux();
    }
}
