package com.threniodine.tmq;


import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Base64;

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
    public String addPlayer(@PathVariable Integer gameId, @RequestBody StringContainer stringContainer){
        String secret = "U0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFE";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                    SignatureAlgorithm.HS256.getJcaName());

        String playerName = stringContainer.payload;
        Integer playerId = gameService.addPlayer(gameId, playerName);
        String jwtToken = Jwts.builder().claim("gameId", gameId).claim("playerId", playerId)
        .signWith(hmacKey).compact();
        return jwtToken;
    }

    @CrossOrigin
    @PostMapping("/getPlayerId")
    public Integer getPlayerId(@RequestBody StringContainer stringContainer){
        String token = stringContainer.payload;
        String secret = "U0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFE";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                    SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);
        Integer temp = jwt.getBody().get("playerId", Integer.class);
        System.out.println(temp);
        return temp;
    }
    /* 
    @CrossOrigin
    @PostMapping("/requestToken/{gameId}/{playerId}")
    public String requestToken(@PathVariable Integer gameId, @PathVariable Integer playerId){
        String jwtToken = Jwts.builder().claim("gameId", gameId).claim("playerId", playerId)
        .signWith(new SecretKeySpec(Base64.getDecoder().decode("U0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFE"), SignatureAlgorithm.HS256.getJcaName())).compact();
        return jwtToken;
    }
    */
    @MessageMapping("/addQuestionSet/{gameId}")
    public void addQuestionSet(QuestionSetContainer qsContainer, @DestinationVariable Integer gameId){
        System.out.println(qsContainer.payload[0].url);
        QuestionContainer qContainer;
        ArrayList<Question> questionSet = new ArrayList<Question>();
        for(int i = 0; i < qsContainer.payload.length; i++){
            qContainer = qsContainer.payload[i];
            questionSet.add(new Question(qContainer));
        }
        gameService.addQuestionSet(gameId, questionSet);
    }

    @MessageMapping("/updatePlayerAnswer/{gameId}/{playerId}")
    public void updatePlayerAnswer(StringAuthContainer stringContainer, @DestinationVariable Integer gameId, @DestinationVariable Integer playerId){
        String token = stringContainer.auth;
        String secret = "U0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFE";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                    SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);
        Integer authPlayerId = jwt.getBody().get("playerId", Integer.class);
        Integer authGameId = jwt.getBody().get("gameId", Integer.class);
        if(gameId == authGameId && playerId == authPlayerId){
            gameService.updatePlayerAnswer(gameId, playerId, stringContainer.payload);
        }
    }

    @MessageMapping("/updatePlayerStatus/{gameId}/{playerId}")
    public void updatePlayerStatus(StringAuthContainer stringContainer, @DestinationVariable Integer gameId, @DestinationVariable Integer playerId){
        String token = stringContainer.auth;
        String secret = "U0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFET0ZTQUxBRE9GU0FMQURPRlNBTEFE";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                    SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);
        Integer authPlayerId = jwt.getBody().get("playerId", Integer.class);
        Integer authGameId = jwt.getBody().get("gameId", Integer.class);
        if(gameId == authGameId && playerId == authPlayerId){
            gameService.updatePlayerStatus(gameId, playerId, stringContainer.payload);
        }
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
