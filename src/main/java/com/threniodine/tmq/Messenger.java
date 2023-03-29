package com.threniodine.tmq;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Data
public class Messenger {
    private Sinks.Many<Game> sink;
    private Flux<Game> flux;

    public Messenger(){
        sink = Sinks.many().multicast().onBackpressureBuffer();
        flux = sink.asFlux();
    }
}
