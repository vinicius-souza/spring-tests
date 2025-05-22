package com.viniciussouza.tests.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> {
    @Override
    <S extends Planet> List<S> findAll(Example<S> example);

    Optional<Planet> findByName(String name);
}
