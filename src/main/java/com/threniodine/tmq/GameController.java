package com.threniodine.tmq;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
	@Autowired
	private SimpMessagingTemplate template;
    private GameService gameService = new GameService(this);

    @CrossOrigin
    @PostMapping("/createGame")
    public Integer createGame(){
        return gameService.createGame();
    }

    @CrossOrigin
    @GetMapping("/readGame/{gameId}")
    public Game readGame(@PathVariable Integer gameId){
        return gameService.readGame(gameId);
    }

    @CrossOrigin
    @PostMapping("/addPlayer/{gameId}")
    public Integer addPlayer(@PathVariable Integer gameId, @RequestBody StringContainer stringContainer){
        String playerName = stringContainer.payload;
        return gameService.addPlayer(gameId, playerName);
    }

    @CrossOrigin
    @PostMapping("/addQuestionSet/{gameId}")
    public Boolean addQuestionSet(@PathVariable Integer gameId, @RequestBody QuestionSetContainer qsContainer){
        System.out.println(qsContainer.payload[0].url);
        QuestionContainer qContainer;
        ArrayList<Question> questionSet = new ArrayList<Question>();
        for(int i = 0; i < qsContainer.payload.length; i++){
            qContainer = qsContainer.payload[i];
            questionSet.add(new Question(qContainer));
        }
        return gameService.addQuestionSet(gameId, questionSet);
    }

    @CrossOrigin
    @PostMapping("/updatePlayerAnswer/{gameId}/{playerId}")
    public Boolean updatePlayerAnswer(@PathVariable Integer gameId, @PathVariable Integer playerId, @RequestBody StringContainer stringContainer){
        String playerAnswer = stringContainer.payload;
        return gameService.updatePlayerAnswer(gameId, playerId, playerAnswer);
    }

    @CrossOrigin
    @PostMapping("/updatePlayerStatus/{gameId}/{playerId}")
    public Boolean updatePlayerStatus(@PathVariable Integer gameId, @PathVariable Integer playerId, @RequestBody StringContainer stringContainer){
        String playerStatus = stringContainer.payload;
        return gameService.updatePlayerStatus(gameId, playerId, playerStatus);
    }
    /*
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateGameState/{id}")
    public Boolean updateGameState(@PathVariable Integer id, @RequestBody GameState gameStateChanges){
        return gameService.updateGameState(id, gameStateChanges);
    }
    */
    @CrossOrigin
    @PostMapping("/deleteGame/{id}")
    public Boolean deleteGame(@PathVariable Integer id){
        return gameService.deleteGame(id);
    }

    public void broadcast(Game g){
        template.convertAndSend("/topic/game/"+g.getGameId(), g);
    }
}
