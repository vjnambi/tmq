package com.threniodine.tmq;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Data;

@Data
public class Player {
    private AtomicBoolean locked;
    private Integer id;
    private String name;
    private String status;
    private Integer score;
    private String answer;

    public Player(String name, Integer id){
        this.locked = new AtomicBoolean(false);
        this.id = id;
        this.name = name;
        this.status = "unready";
        this.score = 0;
        this.answer = "";
    }
}
