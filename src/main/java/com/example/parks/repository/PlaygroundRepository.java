package com.example.parks.repository;

import com.example.parks.model.Playground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
    boolean existsByName(String name);
    Playground findByName(String name);
}
