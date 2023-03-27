package com.threniodine.tmq;

import java.util.Objects;

import org.springframework.messaging.handler.annotation.SendTo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer gameId;
    private String message;

    public void onStart() {

    }

    public void onChange() {
        
    }

    public void change(Game game) {
        if(Objects.nonNull(game.getMessage()) && !"".equalsIgnoreCase(game.getMessage())){
            this.setMessage(game.getMessage());
        }
        onChange();
    }


}
