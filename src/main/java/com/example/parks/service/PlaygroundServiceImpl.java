package com.example.parks.service;

import com.example.parks.model.Playground;
import com.example.parks.repository.PlaygroundRepository;
import com.example.parks.service.interfaces.PlaygroundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaygroundServiceImpl implements PlaygroundService {
    
    private final PlaygroundRepository playgroundRepository;

    public PlaygroundServiceImpl(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
    }

    @Override
    public Playground add(Playground playground) {
        if(playgroundRepository.existsByName(playground.getName()))
            return playgroundRepository.findByName(playground.getName());
        return playgroundRepository.save(playground);
    }

    @Override
    public List<Playground> getAll() {
        return playgroundRepository.findAll();
    }

    @Override
    public Playground get(String name) {
        return playgroundRepository.findByName(name);
    }

    @Override
    public Playground update(Playground playground) {
        return playgroundRepository.save(playground);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        if(playgroundRepository.existsById(id)) {
            playgroundRepository.deleteById(id);
            return new ResponseEntity<>("Park with id " + id + " has been deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Park with id " + id + " cannot be found", HttpStatus.BAD_REQUEST);
    }
}
