package com.cognizant.labs.service;

import com.cognizant.labs.models.Scoreboard;
import com.cognizant.labs.repository.ScoreboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreBoardService {

    @Autowired
    ScoreboardRepository scoreboardRepository;

    public Scoreboard createScoreboard(Scoreboard scoreboard){
        return scoreboardRepository.save(scoreboard);
    }
    public Scoreboard findById(Long id){
        Optional<Scoreboard> currScoreboard = scoreboardRepository.findById(id);
        return currScoreboard.orElse(null);
    }
    public List<Scoreboard> findAll() {
        List<Scoreboard> orderedScores = scoreboardRepository.findAll();
        Collections.sort(orderedScores, Comparator.comparingLong(Scoreboard::getPlayersScore).reversed());
        return orderedScores;
    }
    public Scoreboard updateScoreboard(Scoreboard scoreboard) {
        if(findById(scoreboard.getId()) == null) {
            return null;
        }
        return scoreboardRepository.save(scoreboard);
    }
}
