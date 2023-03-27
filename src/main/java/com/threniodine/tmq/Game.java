package com.threniodine.tmq;

import java.util.Objects;

import org.springframework.messaging.handler.annotation.SendTo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    private Integer gameId;
    @Builder.Default
    private GameState gameState = new GameState();
    @Builder.Default
    private Messenger messenger = new Messenger();

}
