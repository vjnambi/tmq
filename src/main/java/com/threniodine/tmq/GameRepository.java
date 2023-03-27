package com.threniodine.tmq;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface GameRepository extends CrudRepository<Game, Integer> {
    
}
