package com.threniodine.tmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class GameController {

    Sinks.Many<Game> sink = Sinks.many().multicast().onBackpressureBuffer();
    Flux<Game> flux = sink.asFlux();

    @Autowired
    private GameService gameService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createGame")
    public Game createGame(){
        return gameService.createGame();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/readGame")
    public Game readGame(@RequestParam(value = "id") Integer id){
        return gameService.readGame(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateGame")
    public Game updateGame(@RequestParam(value = "id") Integer id, @RequestBody Game gameChanges){
        Game result =  gameService.updateGame(id, gameChanges);
        sink.tryEmitNext(gameService.readGame(1));
        return result;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path="/sse", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> createConnectionAndSendEvents() {
        return flux;//Flux.just(gameService.readGame(1));
    }
}
