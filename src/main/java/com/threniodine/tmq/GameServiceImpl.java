package com.threniodine.tmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameServiceImpl implements GameService {
    
    @Autowired
    private GameRepository gameRepository;

    @Override
    public Game createGame() {
        return gameRepository.save(new Game());
    }

    @Override
    public Game readGame(Integer gameId) {
        return gameRepository.findById(gameId).get();
    }

    @Override
    public Game updateGame(Integer gameId, Game gameChanges) {
        Game target = gameRepository.findById(gameId).get();
        target.change(gameChanges);
        return gameRepository.save(target);
    }
    
}
