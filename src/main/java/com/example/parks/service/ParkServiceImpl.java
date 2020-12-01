package com.example.parks.service;

import com.example.parks.model.Park;
import com.example.parks.repository.ParkRepository;
import com.example.parks.service.interfaces.ParkService;
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
    public List<Park> getAllParks() {
        return null;
    }

    @Override
    public Park getPark(String name) {
        return null;
    }

    @Override
    public Park update(Park park) {
        return null;
    }
}
