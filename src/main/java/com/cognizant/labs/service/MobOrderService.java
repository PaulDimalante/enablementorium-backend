package com.cognizant.labs.service;

import com.cognizant.labs.Enums.ClassListEnum;
import com.cognizant.labs.models.MobOrder;
import com.cognizant.labs.repository.MobOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MobOrderService {

    @Autowired
    MobOrderRepository mobOrderRepository;

    public MobOrder findById(Long id) {
        Optional<MobOrder> currMobOrder = mobOrderRepository.findById(id);
        return currMobOrder.orElse(null);
    }
    public MobOrder createMobOrder(String difficulty) {
        MobOrder currMobOrder = new MobOrder();
        switch(difficulty.toLowerCase()){
            case "easy":
                List<ClassListEnum> mobOrder1 = generateOrder(difficulty);
                currMobOrder = new MobOrder(null, mobOrder1.get(0), mobOrder1.get(1), ClassListEnum.None, ClassListEnum.None, "Easy");
                break;
            case "medium":
                List<ClassListEnum> mobOrder2 = generateOrder(difficulty);
                currMobOrder = new MobOrder(null, mobOrder2.get(0), mobOrder2.get(1), mobOrder2.get(2), ClassListEnum.None, "Medium");
                break;
            case "hard":
                List<ClassListEnum> mobOrder3 = generateOrder(difficulty);
                currMobOrder = new MobOrder(null, mobOrder3.get(0), mobOrder3.get(1), mobOrder3.get(2), mobOrder3.get(3), "Medium");
                break;
        }
        return mobOrderRepository.save(currMobOrder);
    }
    public List<MobOrder> findAll() {
        return mobOrderRepository.findAll();
    }
    public MobOrder updateMobOrder(MobOrder mobOrder){
        if(findById(mobOrder.getId()) == null) {
            return null;
        }
        return mobOrderRepository.save(mobOrder);
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
            Random random = new Random();
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
}
