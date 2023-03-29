package com.threniodine.tmq;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Question {
    private String url;
    private String answer;

    @JsonIgnore
    private static String statesRaw = "Alabama, Alaska, Arizona, Arkansas";
    @JsonIgnore
    private static String[] states = statesRaw.split(", ");
    @JsonIgnore
    private static String capitalsRaw = "Montgomery, Juneau, Phoenix, Little Rock";
    @JsonIgnore
    private static String[] capitals = capitalsRaw.split(", ");

    public Question(){
        Random r = new Random();
        Integer i = r.nextInt(states.length);
        this.url = String.format("What is the capital of %s?", states[i]);
        this.answer = capitals[i];
    }


}
