package com.example.parks.controller;

import com.example.parks.model.Park;
import com.example.parks.service.interfaces.ParkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/parks")
public class ParkController {

    private final ParkService parkService;


    public ParkController(ParkService parkService) {
        this.parkService = parkService;
    }

    @PostMapping("/add")
    public Park add(@Valid @RequestBody Park park) {
        return parkService.add(park);
    }
}
