package com.cognizant.labs;

import com.cognizant.labs.Enums.ClassListEnum;
import com.cognizant.labs.models.MobOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class,args);
    }

    @PostConstruct
    public void saveMobOrders() {
        MobOrder mobOrder1 = new MobOrder(1L, new ArrayList<>(),"Easy");
        MobOrder mobOrder2 = new MobOrder(2L, new ArrayList<>(),"Easy");
        MobOrder mobOrder3 = new MobOrder(3L, new ArrayList<>(),"Easy");
        MobOrder mobOrder4 = new MobOrder(4L, new ArrayList<>(),"Easy");
        MobOrder mobOrder5 = new MobOrder(5L, new ArrayList<>(),"Easy");

        mobOrder1.setOrderOfMobs(generateOrder(mobOrder1.getDifficulty()));
        mobOrder2.setOrderOfMobs(generateOrder(mobOrder2.getDifficulty()));
        mobOrder3.setOrderOfMobs(generateOrder(mobOrder3.getDifficulty()));
        mobOrder4.setOrderOfMobs(generateOrder(mobOrder4.getDifficulty()));
        mobOrder5.setOrderOfMobs(generateOrder(mobOrder5.getDifficulty()));

        List<MobOrder> easyDifficulty = new ArrayList<>();
        easyDifficulty.addAll(Arrays.asList(mobOrder1, mobOrder2, mobOrder3, mobOrder4, mobOrder5));
        easyDifficulty.forEach(mobOrder -> mobOrderService.createMobOrder(mobOrder));

//        ArrayList<Location> locations = new ArrayList<>();
//        locations.addAll(Arrays.asList(location1, location2, location3, location4, location5));
//
//        locations.forEach(location -> locationService.createOfficeLocation(location));

    }

    private List<ClassListEnum> generateOrder(String difficulty) {
        List<ClassListEnum> currList = new ArrayList<>();
        int mobCount = 0;
        switch(difficulty.toLowerCase()) {
            case "easy":
                mobCount = 2;
                break;
            case "medium":
                mobCount = 3;
                break;
            case "hard":
                mobCount = 4;
                break;
        }
        for(int i = 0; i < mobCount; i++) {
            Random random = new Random;
            int rand = random.nextInt(3) + 1;
            switch(rand) {
                case 1:
                    currList.add(ClassListEnum.Fighter);
                    break;
                case 2:
                    currList.add(ClassListEnum.Ranger);
                    break;
                case 3:
                    currList.add(ClassListEnum.Black_Mage);
                    break;
            }
        }
        return currList;
    }

    @PostConstruct
    public void defaultScoreBoard() {

    }

}
