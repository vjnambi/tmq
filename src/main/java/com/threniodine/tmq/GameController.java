package com.threniodine.tmq;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class GameController {

    private ArrayList<Messenger> messengers = new ArrayList<Messenger>();

    @Autowired
    private GameService gameService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createGame")
    public Game createGame(){
        Game answer =  gameService.createGame();
        messengers.add(answer.getGameId()-1, new Messenger());
        return answer;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/readGame/{id}")
    public Game readGame(@PathVariable Integer id){
        return gameService.readGame(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateGame/{id}")
    public Game updateGame(@PathVariable Integer id, @RequestBody Game gameChanges){
        Game result =  gameService.updateGame(id, gameChanges);
        messengers.get(id-1).getSink().tryEmitNext(gameService.readGame(id));
        return result;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path="/sse/{id}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> createConnectionAndSendEvents(@PathVariable Integer id) {
        return messengers.get(id-1).getFlux();//Flux.just(gameService.readGame(1));
    }
}
