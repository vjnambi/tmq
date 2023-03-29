package com.threniodine.tmq;

import lombok.Data;

@Data
public class Player {
    private String name;
    private String status;
    private Integer score;
    private String answer;

    public Player(String name){
        this.name = name;
        this.status = "unready";
        this.score = 0;
        this.answer = "";
    }
}
