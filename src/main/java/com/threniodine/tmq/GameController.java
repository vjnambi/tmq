package com.threniodine.tmq;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @MessageMapping("/addQuestionSet/{gameId}")
    public Boolean addQuestionSet(QuestionSetContainer qsContainer, @DestinationVariable Integer gameId){
        System.out.println(qsContainer.payload[0].url);
        QuestionContainer qContainer;
        ArrayList<Question> questionSet = new ArrayList<Question>();
        for(int i = 0; i < qsContainer.payload.length; i++){
            qContainer = qsContainer.payload[i];
            questionSet.add(new Question(qContainer));
        }
        return gameService.addQuestionSet(gameId, questionSet);
    }

    @MessageMapping("/updatePlayerAnswer/{gameId}/{playerId}")
    public Boolean updatePlayerAnswer(StringContainer stringContainer, @DestinationVariable Integer gameId, @DestinationVariable Integer playerId){
        return gameService.updatePlayerAnswer(gameId, playerId, stringContainer.payload);
    }

    @MessageMapping("/updatePlayerStatus/{gameId}/{playerId}")
    public Boolean updatePlayerStatus(StringContainer stringContainer, @DestinationVariable Integer gameId, @DestinationVariable Integer playerId){
        return gameService.updatePlayerStatus(gameId, playerId, stringContainer.payload);
    }
    /*
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/updateGameState/{id}")
    public Boolean updateGameState(@PathVariable Integer id, @RequestBody GameState gameStateChanges){
        return gameService.updateGameState(id, gameStateChanges);
    }
    
    @CrossOrigin
    @PostMapping("/deleteGame/{id}")
    public Boolean deleteGame(@PathVariable Integer id){
        return gameService.deleteGame(id);
    }

    @MessageMapping("/changeTitle/{id}")
    public Boolean changeGameTitle(StringContainer stringContainer, @DestinationVariable Integer id){
        return gameService.changeGameTitle(id,stringContainer.payload);
    }
    */
    public void broadcast(Game g){
        template.convertAndSend("/topic/game/"+g.getGameId(), g);
    }
}
