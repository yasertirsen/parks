package com.example.parks.service;

import com.example.parks.exceptions.ParkNotFoundException;
import com.example.parks.model.Park;
import com.example.parks.repository.ParkRepository;
import com.example.parks.service.interfaces.ParkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkServiceImpl implements ParkService {

    private final ParkRepository parkRepository;

    public ParkServiceImpl(ParkRepository parkRepository) {
        this.parkRepository = parkRepository;
    }

    @Override
    public Park add(Park park) {
        if(parkRepository.existsByName(park.getName()))
            return parkRepository.findByName(park.getName());
        return parkRepository.save(park);
    }

    @Override
    public List<Park> getAll() {
        return parkRepository.findAll();
    }

    @Override
    public Park get(String name) {
        return parkRepository.findByName(name);
    }

    @Override
    public Park update(Park park) throws ParkNotFoundException {
        if(parkRepository.existsById(park.getId()))
            return parkRepository.save(park);
        throw new ParkNotFoundException();
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        if(parkRepository.existsById(id)) {
            parkRepository.deleteById(id);
            return new ResponseEntity<>("Park with id " + id + " has been deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Park with id " + id + " cannot be found", HttpStatus.BAD_REQUEST);
    }
}
