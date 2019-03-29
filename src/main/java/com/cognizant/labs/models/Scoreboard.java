package com.cognizant.labs.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="scoreBoard")
@Table(name = "scoreBoard")
public class Scoreboard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long playersScore;

    private String playersName;

    private String playersClass;

    private String currentDifficulty;
}
