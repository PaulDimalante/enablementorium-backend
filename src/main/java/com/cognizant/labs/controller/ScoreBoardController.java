package com.cognizant.labs.controller;

import com.cognizant.labs.models.Scoreboard;
import com.cognizant.labs.service.ScoreBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/scoreboard")
public class ScoreBoardController {

    @Autowired
    ScoreBoardService scoreBoardService;

    @GetMapping
    public ResponseEntity<List<Scoreboard>> getWholeScoreboard() {
        List<Scoreboard> scoreboardList = scoreBoardService.findAll();
        if(scoreboardList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scoreboardList, HttpStatus.OK);
    }

    @GetMapping("/{scoreboardId}")
    public ResponseEntity<Scoreboard> getScoreboardById(@PathVariable Long id) {
        Scoreboard scoreboard = scoreBoardService.findById(id);
        if(scoreboard == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scoreboard, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Scoreboard> addAScoreToTheScoreboard(@RequestBody Scoreboard scoreboard){
        return new ResponseEntity<>(scoreBoardService.createScoreboard(scoreboard), HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<Scoreboard> updateAScoreOnTheScoreboard(@RequestBody Scoreboard scoreboard){
        Scoreboard result = scoreBoardService.updateScoreboard(scoreboard);
        if(result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
