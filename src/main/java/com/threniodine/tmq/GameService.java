package com.threniodine.tmq;

public interface GameService {
    public Game createGame();
    public Game readGame(Integer gameId);
    public Game updateGame(Integer gameId, Game gameChanges);
}
