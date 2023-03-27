package com.threniodine.tmq;

import java.util.Objects;

import lombok.Data;

@Data
public class GameState {
    private String message = "";

    public Boolean accept(GameState gameStateChanges){
        Boolean b = false;
        if(Objects.nonNull(gameStateChanges.getMessage()) && !"".equalsIgnoreCase(gameStateChanges.getMessage())){
            this.setMessage(gameStateChanges.getMessage());
            b = true;
        }
        return b;
    }
}
