package com.viniciussouza.tests.web;

import com.viniciussouza.tests.domain.Planet;
import com.viniciussouza.tests.domain.PlanetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetService planetService;

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {
        var planetCreated = this.planetService.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Planet> getById(@PathVariable("id") Long id){
        return this.planetService.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable("name") String name){
        return this.planetService.getByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> list (@RequestParam(required = false) String climate, @RequestParam(required = false) String terrain){
        var list = planetService.list(climate, terrain);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        this.planetService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
