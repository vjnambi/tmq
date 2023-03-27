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

    private GameService gameService = new GameService();

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createGame")
    public Integer createGame(){
        return gameService.createGame();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/readGameState/{id}")
    public GameState readGameState(@PathVariable Integer id){
        return gameService.readGameState(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateGameState/{id}")
    public Boolean updateGameState(@PathVariable Integer id, @RequestBody GameState gameStateChanges){
        return gameService.updateGameState(id, gameStateChanges);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/deleteGame/{id}")
    public Boolean deleteGame(@PathVariable Integer id){
        return gameService.deleteGame(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path="/subscribeGame/{id}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GameState> subscribeGame(@PathVariable Integer id) {
        return gameService.subscribeGame(id);
    }
}
