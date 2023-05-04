package com.threniodine.tmq;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
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
    private ArrayList<Question> questionPool;
    private AtomicBoolean questionPoolLock;
    private ArrayList<Question> questionList;
    private Integer currentQuestionNum;
    private Question currentQuestion;
    private String status;
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
        questionPool = new ArrayList<Question>();
        questionPoolLock = new AtomicBoolean(false);
        questionList = new ArrayList<Question>();
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

    public Boolean addQuestionSet(ArrayList<Question> qs){
        while(questionPoolLock.compareAndSet(false, true)){

        }
        Boolean b = true;
        int s = qs.size();
        for(int i = 0; i < s; i++){
            if(!addQuestion(qs.get(i))){
                b = false;
            }
        }
        questionPoolLock.compareAndSet(true, false);
        return b;
    }

    public Boolean addQuestion(Question q){
        questionPool.add(q);
        return true;
    }

    public Boolean updatePlayerAnswer(Integer playerId, String playerAnswer){
        Player p = playerList[playerId-1];
        Boolean b = false;
        while(p.getLocked().compareAndSet(false, true)){

        }
        if(!"".equals(playerAnswer) && p.getStatus().equals("thinking")){
            p.setAnswer(playerAnswer);
            p.setStatus("answered");
            b = true;
        }
        p.getLocked().compareAndSet(true, false);
        return b;
    }

    public Boolean updatePlayerStatus(Integer playerId, String playerStatus){
        Player p = playerList[playerId-1];
        Boolean b = false;
        while(p.getLocked().compareAndSet(false, true)){

        }
        String s = p.getStatus();
        if(playerStatus.equals("ready") && s.equals("unready") || playerStatus.equals("unready") && s.equals("ready")){
            b = true;
            p.setStatus(playerStatus);
        }
        p.getLocked().compareAndSet(true, false);

        return b;
    }

    public void publishGame(){
        messenger.getSink().tryEmitNext(this);
    }

    public Boolean checkAllPlayers(String checkStatus){
        Boolean b = true;
        Boolean any = false;
        Player p;

        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p)){
                any = true;
                if(!p.getStatus().equals(checkStatus)){
                    b = false;
                }
            }
        }

        return any && b;
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
            if(Objects.nonNull(p) && p.getAnswer().equalsIgnoreCase(correctAnswer)){
                p.setScore(p.getScore() + 1);
            }
        }
    }

    public void transitionNextQuestion(){
        Player p;
        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p)){
                while(p.getLocked().compareAndSet(false, true)){

                }
                p.setStatus("thinking");
                p.setAnswer("");
                p.getLocked().compareAndSet(true, false);

            }
        }
        if(currentQuestionNum == questionList.size()){
            status = "result";
        } else {
            currentQuestionNum++;
            currentQuestion = questionList.get(currentQuestionNum-1);
            timeKeeper.startTimer(15);
            status = "question";
        }
    }

    public void transitionAnswer(){
        Player p;
        for(int i = 0; i < 4; i++){
            p = playerList[i];
            if(Objects.nonNull(p)){
                while(p.getLocked().compareAndSet(false, true)){

                }
                p.setStatus("unready");
                p.getLocked().compareAndSet(true, false);

            }
        }
        status = "answer";
        timeKeeper.cancelTimer();
    }

    public void populateQuestionList(){
        Random r = new Random();
        int s = questionPool.size();
        for(int i = 0; i < 10; i++){
            questionList.add(questionPool.get(r.nextInt(s)));
        }
    }

    public void transition(){
        if(status.equals("lobby")){
            Boolean allReady = checkAllPlayers("ready");
            while(questionPoolLock.compareAndSet(false, true)){

            }
            if(allReady && questionPool.size() > 0){
                populateQuestionList();
                transitionNextQuestion();
            }
            questionPoolLock.compareAndSet(true, false);
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
