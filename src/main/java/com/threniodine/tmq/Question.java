package com.threniodine.tmq;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Question {
    private String url;
    private String answer;

    public Question(QuestionContainer qContainer){
        this.url = qContainer.url;
        this.answer = qContainer.answer;
    }


}
