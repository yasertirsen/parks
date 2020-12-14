package com.example.parks.controller;

import com.example.parks.exceptions.ParkNotFoundException;
import com.example.parks.model.Playground;
import com.example.parks.service.interfaces.PlaygroundService;
import com.example.parks.shared.XMLParser;
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
@RequestMapping("/api/playgrounds")
public class PlaygroundController {

    private final XMLParser xmlParser;
    private final PlaygroundService playgroundService;

    @Autowired
    public PlaygroundController(XMLParser xmlParser, PlaygroundService playgroundService) {
        this.xmlParser = xmlParser;
        this.playgroundService = playgroundService;
    }

    @PostMapping("/add")
    public Playground add(@Valid @RequestBody Playground playground) {
        return playgroundService.add(playground);
    }

    @GetMapping("/all")
    public List<Playground> getAllPlayground() {
        return playgroundService.getAll();
    }

    @GetMapping("/{name}")
    public Playground getPlayground(@PathVariable String name) {
        return playgroundService.get(name);
    }

    @PutMapping("/update")
    public Playground updatePlayground(@RequestBody Playground playground) throws ParkNotFoundException {
        return playgroundService.update(playground);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlayground(@PathVariable Long id) {
        return playgroundService.delete(id);
    }

    @GetMapping("/parse")
    public List<Playground> getAllPlaygrounds() {
        return xmlParser.parsePlaygrounds();
    }
}
