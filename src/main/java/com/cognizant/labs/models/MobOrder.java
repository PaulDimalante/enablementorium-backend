package com.cognizant.labs.models;

import com.cognizant.labs.Enums.ClassListEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="mobOrder")
@Table(name = "mob_order")
public class MobOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ClassListEnum mob1;

    private ClassListEnum mob2;

    private ClassListEnum mob3;

    private ClassListEnum mob4;

    private String difficulty;

//    public MobOrder(Long id, ClassListEnum mob1, ClassListEnum mob2, ClassListEnum mob3, ClassListEnum mob4, String difficulty) {
//        this.mob1 = mob1;
//        this.mob2 = mob2;
//        this.mob3 = mob3;
//        this.mob4 = mob4;
//        this.difficulty = difficulty;
//    }
//
//    public MobOrder() {
//
//    }

    public Long getId() {
        return this.id;
    }
}



