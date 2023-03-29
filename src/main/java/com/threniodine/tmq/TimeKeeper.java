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
    private Game parentGame;

    public TimeKeeper(Game parentGame){
        this.time = 0;
        this.parentGame = parentGame;
    }

    class CountTask extends TimerTask {
        public void run() {
            time = time - 1;
            if(time <= 0){
                timer.cancel();
                parentGame.transition();
            }
            parentGame.getMessenger().getSink().tryEmitNext(parentGame);
        }
    }

    public void startTimer(Integer duration){
        this.timer = new Timer();
        this.time = duration+1;
        timer.scheduleAtFixedRate(new CountTask(), 0, 1000);
    }

    public void cancelTimer(){
        this.timer.cancel();
    }

}
