package com.threniodine.tmq;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Question {
    private String url;
    private String answer;

    @JsonIgnore
    private static String statesRaw = "HZvnP0svJzk, w279PjazOAw, zUvEHNW6y_A";
    @JsonIgnore
    private static String[] states = statesRaw.split(", ");
    @JsonIgnore
    private static String capitalsRaw = "A Dream That is More Scarlet Than Red, A Soul as Red as a Ground Cherry, Apparitions Stalk the Night";
    @JsonIgnore
    private static String[] capitals = capitalsRaw.split(", ");

    public Question(){
        Random r = new Random();
        Integer i = r.nextInt(states.length);
        this.url = states[i];
        this.answer = capitals[i];
    }


}
