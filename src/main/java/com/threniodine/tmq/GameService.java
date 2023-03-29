package com.threniodine.tmq;

import java.util.Objects;

import reactor.core.publisher.Flux;

public class GameService {

    private GameDB gameDB;

    public GameService() {
        this.gameDB = new GameDB();
    }
    
    public Integer createGame(){
        Game g = new Game();
        Integer i = g.getGameId();
        gameDB.getGameMap().put(i, g);
        return i;
    }

    public Game readGame(Integer gameId){
        return gameDB.getGameMap().get(gameId);
    }

    public GameState readGameState(Integer gameId){
        return readGame(gameId).getGameState();
    }

    public Integer addPlayer(Integer gameId, String playerName){
        GameState gs = readGameState(gameId);
        Integer i = gs.addPlayer(playerName);
        if(i > 0){
            gs.transition();
            publishGameState(gameId);
        }
        return i;
    }

    public Boolean updatePlayerAnswer(Integer gameId, Integer playerId, String playerAnswer){
        GameState gs = readGameState(gameId);
        Boolean b = gs.updatePlayerAnswer(playerId, playerAnswer);
        if(b){
            gs.transition();
            publishGameState(gameId);
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

    public Flux<GameState> subscribeGame(Integer gameId){
        return readGame(gameId).getMessenger().getFlux();
    }

    public void publishGameState(Integer gameId){
        readGame(gameId).publishGameState();
    }


}
