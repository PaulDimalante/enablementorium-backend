package com.cognizant.labs;

import com.cognizant.labs.Enums.ClassListEnum;
import com.cognizant.labs.models.MobOrder;
import com.cognizant.labs.models.Scoreboard;
import com.cognizant.labs.service.MobOrderService;
import com.cognizant.labs.service.ScoreBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Application {

    @Autowired
    MobOrderService mobOrderService;

    @Autowired
    ScoreBoardService scoreBoardService;

    public static void main(String... args) {
        SpringApplication.run(Application.class,args);
    }

    @PostConstruct
    public void setupMobOrderAndScoreboard() {
        for(long i = 0; i < 15; i++){
            if (0 <= i && i < 5) {
                mobOrderService.createMobOrder("Easy");
            }
            else if (5 <= i && i < 10) {
                mobOrderService.createMobOrder("Medium");
            }
            else if (10 <= i && i < 15) {
                mobOrderService.createMobOrder("Hard");
            }
        }

        Scoreboard scoreboard1 = new Scoreboard(1L, 192L, "MikeANike", "Black Mage", "Hard");
        Scoreboard scoreboard2 = new Scoreboard(2L, 20L, "Ade", "Fighter", "Easy");
        Scoreboard scoreboard3 = new Scoreboard(3L, 121L, "Shariq", "Black Mage", "Medium");
        Scoreboard scoreboard4 = new Scoreboard(4L, 221L, "Kat", "Ranger", "Hard");
        Scoreboard scoreboard5 = new Scoreboard(5L, 90L, "Michael Da Underwood", "Fighter", "Medium");
        Scoreboard scoreboard6 = new Scoreboard(6L, 270L, "Zaryn", "Black Mage", "Hard");

        scoreBoardService.createScoreboard(scoreboard1);
        scoreBoardService.createScoreboard(scoreboard2);
        scoreBoardService.createScoreboard(scoreboard3);
        scoreBoardService.createScoreboard(scoreboard4);
        scoreBoardService.createScoreboard(scoreboard5);
        scoreBoardService.createScoreboard(scoreboard6);
    }

}
