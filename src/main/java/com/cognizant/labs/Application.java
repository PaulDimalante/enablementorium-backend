package com.cognizant.labs;

import com.cognizant.labs.Enums.ClassListEnum;
import com.cognizant.labs.models.MobOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class,args);
    }

    @PostConstruct
    public void saveMobOrders() {
        MobOrder mobOrder1 = new MobOrder(1L, new ArrayList<>());
        MobOrder mobOrder2 = new MobOrder(1L, new ArrayList<>());
        MobOrder mobOrder3 = new MobOrder(1L, new ArrayList<>());
        MobOrder mobOrder4 = new MobOrder(1L, new ArrayList<>());
        MobOrder mobOrder5 = new MobOrder(1L, new ArrayList<>());

        mobOrder1.setOrderOfMobs(generateOrder("Easy"));
        mobOrder2.setOrderOfMobs(generateOrder("Easy"));
        mobOrder3.setOrderOfMobs(generateOrder("Easy"));
        mobOrder4.setOrderOfMobs(generateOrder("Easy"));
        mobOrder5.setOrderOfMobs(generateOrder("Easy"));

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
