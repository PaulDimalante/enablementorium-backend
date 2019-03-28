package com.cognizant.labs.models;

import com.cognizant.labs.Enums.ClassListEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private List<ClassListEnum> orderOfMobs = new ArrayList<>();

}



