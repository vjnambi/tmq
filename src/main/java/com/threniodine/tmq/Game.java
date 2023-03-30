package com.threniodine.tmq;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Game {
    static AtomicInteger nextGameId = new AtomicInteger(0);

    private Integer gameId;
    private String gameName;
    private Player[] playerList;
    @JsonIgnore
    private Question[] questionList;
    private Integer currentQuestionNum;
    private Question currentQuestion;
    private String status;
    @JsonIgnore
    private AtomicBoolean[] playerListAvailability;
    @JsonIgnore
    private Messenger messenger;
    private TimeKeeper timeKeeper;
    

    public Game(){
        this.gameId = nextGameId.incrementAndGet();

        this.gameName = "Thren's Music Quiz";

        this.messenger = new Messenger();
        this.timeKeeper = new TimeKeeper(this);

        playerList = new Player[4];
        playerListAvailability = new AtomicBoolean[4];
        for(int i = 0; i < 4; i++){
            playerListAvailability[i] = new AtomicBoolean(false);
        }

        questionList = new Question[10];
        for(int i = 0; i < 10; i++){
            questionList[i] = new Question();
        }
        currentQuestionNum = 0;
        status = "lobby";

    }

    public Integer addPlayer(String playerName){
        for(int i = 0; i < 4; i++){
            if(playerListAvailability[i].compareAndSet(false, true)){
                playerList[i] = new Player(playerName);
                return i+1;
            }
        }
        return -1;
    }

    public Boolean updatePlayerAnswer(Integer playerId, String playerAnswer){
        Player p = playerList[playerId-1];
        p.setAnswer(playerAnswer);
        if(playerAnswer.compareToIgnoreCase("ready") == 0 || playerAnswer.compareToIgnoreCase("r") == 0){
            p.setStatus("ready");
        } else {
            p.setStatus("answered");
        }
        return true;
    }

    public void publishGame(){
        messenger.getSink().tryEmitNext(this);
    }

    public Boolean checkAllPlayers(String checkStatus){
        Boolean b = true;
        Player p;

        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p) && !p.getStatus().equals(checkStatus)){
                b = false;
            }
        }

        return b;
    }

    public Boolean checkAnyPlayers(String checkStatus){
        Boolean b = false;
        Player p;

        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p) && p.getStatus().equals(checkStatus)){
                b = true;
            }
        }

        return b;
    }

    public void gradePlayers(String correctAnswer){
        Player p;
        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p) && p.getAnswer().equals(correctAnswer)){
                p.setScore(p.getScore() + 1);
            }
        }
    }

    public void transitionNextQuestion(){
        Player p;
        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p)){
                p.setStatus("thinking");
                p.setAnswer("");
            }
        }
        if(currentQuestionNum == 10){
            status = "result";
        } else {
            currentQuestionNum++;
            currentQuestion = questionList[currentQuestionNum-1];
            timeKeeper.startTimer(15);
            status = "question";
        }
    }

    public void transitionAnswer(){
        Player p;
        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p)){
                p.setStatus("unready");
            }
        }
        status = "answer";
        timeKeeper.cancelTimer();
    }

    public void transition(){
        if(status.equals("lobby")){
            Boolean allReady = checkAllPlayers("ready");
            if(allReady){
                transitionNextQuestion();
            }
        } else if(status.equals("question")){
            Boolean AllAnswered = checkAllPlayers("answered");
            if(AllAnswered || timeKeeper.getTime() == 0){
                gradePlayers(currentQuestion.getAnswer());
                transitionAnswer();
            }
        } else if(status.equals("answer")){
            Boolean allReady = checkAllPlayers("ready");
            if(allReady){
                transitionNextQuestion();
            }
        } else if(status.equals("result")){

        } else {
            System.out.println("TRANSITION WHILE STATUS IS NULL");
        }
    }

    /*
    public Boolean accept(GameState gameStateChanges){
        Boolean b = false;
        if(Objects.nonNull(gameStateChanges.getMessage()) && !"".equalsIgnoreCase(gameStateChanges.getMessage())){
            this.setMessage(gameStateChanges.getMessage());
            b = true;
        }
        return b;
    }
    */
}