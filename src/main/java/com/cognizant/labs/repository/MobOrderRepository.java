package com.cognizant.labs.repository;

import com.cognizant.labs.models.MobOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobOrderRepository extends JpaRepository<MobOrder, Long> {
}
