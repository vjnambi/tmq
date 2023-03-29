package com.threniodine.tmq;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;

@Data
public class GameState {
    private String message;
    private Player[] playerList;
    private AtomicBoolean[] playerListAvailability;
    private Messenger parentMessenger;
    private TimeKeeper timeKeeper;
    

    public GameState(Messenger parentMessenger){
        playerList = new Player[4];
        playerListAvailability = new AtomicBoolean[4];
        for(int i = 0; i < 4; i++){
            playerListAvailability[i] = new AtomicBoolean(false);
        }
        this.parentMessenger = parentMessenger;
        this.timeKeeper = new TimeKeeper(this);
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
