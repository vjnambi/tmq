package com.threniodine.tmq;

import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TimeKeeper {
    private Integer time;
    @JsonIgnore
    private Timer timer;
    @JsonIgnore
    private GameState parentGameState;

    public TimeKeeper(GameState parentGameState){
        this.time = 0;
        this.parentGameState = parentGameState;
    }

    class CountTask extends TimerTask {
        public void run() {
            time = time - 1;
            if(time <= 0){
                timer.cancel();
                parentGameState.transition();
            }
            parentGameState.getParentMessenger().getSink().tryEmitNext(parentGameState);
        }
    }

    public void startTimer(Integer duration){
        this.timer = new Timer();
        this.time = duration+1;
        timer.scheduleAtFixedRate(new CountTask(), 0, 1000);
    }



}
