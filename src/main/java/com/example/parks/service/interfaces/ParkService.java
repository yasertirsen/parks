package com.example.parks.service.interfaces;

import com.example.parks.model.Park;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ParkService {

    Park add(Park park);

    List<Park> getAll();

    Park get(String name);

    Park update(Park park);

    ResponseEntity<String> delete(Long id);
}
