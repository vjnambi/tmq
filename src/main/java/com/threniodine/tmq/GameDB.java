package com.threniodine.tmq;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class GameDB{
    private Map<Integer, Game> gameMap;

    public GameDB(){
        this.gameMap = new HashMap<Integer, Game>();
    }
}