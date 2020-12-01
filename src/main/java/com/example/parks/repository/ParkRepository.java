package com.example.parks.repository;

import com.example.parks.model.Park;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkRepository extends JpaRepository<Park, Long> {
    boolean existsByName(String name);
    Park findByName(String name);
}
