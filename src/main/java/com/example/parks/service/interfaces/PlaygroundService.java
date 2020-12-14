package com.example.parks.service.interfaces;

import com.example.parks.exceptions.ParkNotFoundException;
import com.example.parks.model.Playground;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlaygroundService {
    Playground add(Playground playground);

    List<Playground> getAll();

    Playground get(String name);

    Playground update(Playground playground) throws ParkNotFoundException;

    ResponseEntity<String> delete(Long id);
}
