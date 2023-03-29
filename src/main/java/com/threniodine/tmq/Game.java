package com.threniodine.tmq;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Game {
    static AtomicInteger nextGameId = new AtomicInteger(0);

    private Integer gameId;
    private String message;
    private Player[] playerList;
    @JsonIgnore
    private AtomicBoolean[] playerListAvailability;
    @JsonIgnore
    private Messenger messenger;
    private TimeKeeper timeKeeper;
    

    public Game(){
        this.gameId = nextGameId.incrementAndGet();

        this.messenger = new Messenger();
        this.timeKeeper = new TimeKeeper(this);

        playerList = new Player[4];
        playerListAvailability = new AtomicBoolean[4];
        for(int i = 0; i < 4; i++){
            playerListAvailability[i] = new AtomicBoolean(false);
        }

    }

    public Integer addPlayer(String playerName){
        for(int i = 0; i < 4; i++){
            if(playerListAvailability[i].compareAndSet(false, true)){
                playerList[i] = new Player(playerName);
                return i+1;
            }
        }
        return -1;
    }

    public Boolean updatePlayerAnswer(Integer playerId, String playerAnswer){
        playerList[playerId-1].setAnswer(playerAnswer);
        return true;
    }

    public void publishGame(){
        messenger.getSink().tryEmitNext(this);
    }

    public void transition(){

        Integer chungusNum = 0;
        Integer playerNum = 0;

        Boolean anyTimer = false;
        Boolean timerActive = timeKeeper.getTime() > 0;

        Player p;
        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p)){
                playerNum++;
                if(!timerActive && p.getAnswer().equals("Timer")){
                    anyTimer = true;
                }
                if(p.getAnswer().equals("Big Chungus")){
                    chungusNum++;
                }
            }

        }

        if(playerNum > 0 && playerNum == chungusNum){
            message = "BIG CHUNGUS";
        } else {
            message = "";
        }

        if(anyTimer){
            timeKeeper.startTimer(15);
        }


    }

    /*
    public Boolean accept(GameState gameStateChanges){
        Boolean b = false;
        if(Objects.nonNull(gameStateChanges.getMessage()) && !"".equalsIgnoreCase(gameStateChanges.getMessage())){
            this.setMessage(gameStateChanges.getMessage());
            b = true;
        }
        return b;
    }
    */
}
