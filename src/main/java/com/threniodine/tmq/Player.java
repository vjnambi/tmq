package com.threniodine.tmq;

import lombok.Data;

@Data
public class Player {
    private String name;
    private String answer;

    public Player(String name){
        this.name = name;
        this.answer = "";
    }
}
