package com.example.parks.service.interfaces;

import com.example.parks.model.Park;

import java.util.List;

public interface ParkService {

    Park add(Park park);

    List<Park> getAllParks();

    Park getPark(String name);

    Park update(Park park);
}
