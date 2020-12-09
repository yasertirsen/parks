package com.example.parks.controller;

import com.example.parks.exceptions.ParsingException;
import com.example.parks.model.Park;
import com.example.parks.service.interfaces.ParkService;
import com.example.parks.shared.HTMLParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/parks")
public class ParkController {

    private final ParkService parkService;
    private final HTMLParser htmlParser;

    @Autowired
    public ParkController(ParkService parkService, HTMLParser htmlParser) {
        this.parkService = parkService;
        this.htmlParser = htmlParser;
    }

    @PostMapping("/add")
    public Park add(@Valid @RequestBody Park park) {
        return parkService.add(park);
    }

    @GetMapping("/all")
    public List<Park> getAllParks() {
        return parkService.getAll();
    }

    @GetMapping("/{name}")
    public Park getPark(@PathVariable String name) {
        return parkService.get(name);
    }

    @PutMapping("/update")
    public Park updatePark(@RequestBody Park park) {
        return parkService.update(park);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePark(@PathVariable Long id) {
        return parkService.delete(id);
    }

    @GetMapping("/parseDublin")
    public List<Park> getAllDublinParks() throws ParsingException {
        return htmlParser.parseDublinParks();
    }

    @GetMapping("/parseDunLao")
    public List<Park> getAllDunLaoParks() throws ParsingException {
        return htmlParser.parseDunLaoParks();
    }
}
