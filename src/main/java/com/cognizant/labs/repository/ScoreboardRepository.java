package com.cognizant.labs.repository;


import com.cognizant.labs.models.Scoreboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreboardRepository extends JpaRepository<Scoreboard, Long> {
}
