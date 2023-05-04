package com.threniodine.tmq;

import java.util.ArrayList;
import java.util.Objects;

import reactor.core.publisher.Flux;

public class GameService {

    private GameDB gameDB;
    private GameController gc;

    public GameService(GameController gc) {
        this.gc = gc;
        this.gameDB = new GameDB();
    }
    
    public Integer createGame(){
        Game g = new Game(gc);
        Integer i = g.getGameId();
        gameDB.getGameMap().put(i, g);
        return i;
    }

    public Game readGame(Integer gameId){
        return gameDB.getGameMap().get(gameId);
    }

    public Integer addPlayer(Integer gameId, String playerName){
        Game g = readGame(gameId);
        Integer i = g.addPlayer(playerName);
        if(i > 0){
            g.transition();
            g.publishGame();
        }
        return i;
    }

    public Boolean addQuestionSet(Integer gameId, ArrayList<Question> questionSet){
        Game g = readGame(gameId);
        Boolean b = g.addQuestionSet(questionSet);
        if(b){
            g.transition();
            g.publishGame();
        }
        return b;
    }

    public Boolean updatePlayerAnswer(Integer gameId, Integer playerId, String playerAnswer){
        Game g = readGame(gameId);
        Boolean b = g.updatePlayerAnswer(playerId, playerAnswer);
        if(b){
            g.transition();
            g.publishGame();
        }
        return b;
    }

    public Boolean updatePlayerStatus(Integer gameId, Integer playerId, String playerStatus){
        Game g = readGame(gameId);
        Boolean b = g.updatePlayerStatus(playerId, playerStatus);
        if(b){
            g.transition();
            g.publishGame();
        }
        return b;
    }
    /*
    public Boolean updateGameState(Integer id, GameState gameStateChanges){
        Boolean b = gameDB.getGameMap().get(id).getGameState().accept(gameStateChanges);
        if(b){
            Game g = gameDB.getGameMap().get(id);
            GameState gs = g.getGameState();
            g.getMessenger().getSink().tryEmitNext(gs);
        }
        return b;
    }
    */
    public Boolean deleteGame(Integer gameId){
        Game g = gameDB.getGameMap().remove(gameId);
        return Objects.nonNull(g);
    }

    public Flux<Game> subscribeGame(Integer gameId){
        return readGame(gameId).getMessenger().getFlux();
    }

    public void publishGameState(Integer gameId){
        readGame(gameId).publishGame();
    }


}
