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

}



