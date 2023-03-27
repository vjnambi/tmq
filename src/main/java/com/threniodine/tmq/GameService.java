package com.threniodine.tmq;

import java.util.Objects;

import reactor.core.publisher.Flux;

public class GameService {

    private GameDB gameDB = new GameDB();
    private Integer nextGameId = 1;
    
    public Integer createGame(){
        Integer i = nextGameId;
        nextGameId += 1;

        Game g = new Game();
        g.setGameId(i);
        gameDB.getGameMap().put(i, g);
        return i;
    }

    public GameState readGameState(Integer id){
        return gameDB.getGameMap().get(id).getGameState();
    }

    public Boolean updateGameState(Integer id, GameState gameStateChanges){
        Boolean b = gameDB.getGameMap().get(id).getGameState().accept(gameStateChanges);
        if(b){
            Game g = gameDB.getGameMap().get(id);
            GameState gs = g.getGameState();
            g.getMessenger().getSink().tryEmitNext(gs);
        }
        return b;
    }

    public Boolean deleteGame(Integer id){
        Game g = gameDB.getGameMap().remove(id);
        return Objects.nonNull(g);
    }

    public Flux<GameState> subscribeGame(Integer id){
        return gameDB.getGameMap().get(id).getMessenger().getFlux();
    }


}
