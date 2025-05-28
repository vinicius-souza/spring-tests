package com.viniciussouza.tests.domain;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return this.planetRepository.save(planet);
    }

    public Optional<Planet> get(Long id){
        return this.planetRepository.findById(id);
    }

    public Optional<Planet> getByName(String name){
        return this.planetRepository.findByName(name);
    }

    public List<Planet> list(String climate, String terrain){
        Example<Planet> query = QueryBuilder.build(new Planet(climate, terrain));
        return this.planetRepository.findAll(query);
    }

    public void remove(Long id){
        this.planetRepository.deleteById(id);
    }
}
