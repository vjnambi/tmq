package com.threniodine.tmq;

import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.messaging.handler.annotation.SendTo;

import lombok.Data;

@Data
public class Game {
    static AtomicInteger nextGameId = new AtomicInteger(0);

    private Integer gameId;
    private GameState gameState;
    private Messenger messenger;

    public Game(){
        this.gameId = nextGameId.incrementAndGet();
        this.messenger = new Messenger();
        this.gameState = new GameState(this.messenger);
        
    }

    public void publishGameState(){
        getMessenger().getSink().tryEmitNext(gameState);
    }


}
