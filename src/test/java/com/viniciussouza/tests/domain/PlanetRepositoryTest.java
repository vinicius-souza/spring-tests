package com.viniciussouza.tests.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.viniciussouza.tests.common.PlanetConstants.PLANET;
import static com.viniciussouza.tests.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Anotação SpringBootTest não necessária quando utilizado o DataJpaTest
//@SpringBootTest(classes = PlanetRepository.class)
@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach() {
        PLANET.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        var planetSaved = this.planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planetSaved.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());

    }

    @ParameterizedTest
    @MethodSource("providesInvalidPlanets")
    public void createPlanet_WithInvalidData_ThrowsException(Planet planet){
        assertThatThrownBy(() -> this.planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    private static Stream<Arguments> providesInvalidPlanets() {
        return Stream.of(
                Arguments.of(new Planet(null, "Climate", "Terrain")),
                Arguments.of(new Planet("Name", null, "Terrain")),
                Arguments.of(new Planet("Name", "Climate", null)),
                Arguments.of(new Planet(null, null, "Terrain")),
                Arguments.of(new Planet(null, "Climate", null)),
                Arguments.of(new Planet("Name", null, null)),
                Arguments.of(new Planet(null, null, null)),
                Arguments.of(new Planet("", "Climate", "Terrain")),
                Arguments.of(new Planet("Name", "", "Terrain")),
                Arguments.of(new Planet("Name", "Climate", "")),
                Arguments.of(new Planet("Name", "", "Terrain")),
                Arguments.of(new Planet("", "Climate", "Terrain")),
                Arguments.of(new Planet("Name", "", "")),
                Arguments.of(new Planet("", "Climate", "")),
                Arguments.of(new Planet("", "", "Terrain")),
                Arguments.of(new Planet("", "", ""))
        );
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException(){

        //Necessário a utilização do TestEntityManager porque o save do planetRepository é o objeto de teste,
        //logo, não se pode usá-lo fora do cenário de teste.
        var planetSaved = this.testEntityManager.persistFlushFind(PLANET);
        this.testEntityManager.detach(planetSaved);
        planetSaved.setId(null);

        assertThatThrownBy(() -> this.planetRepository.save(planetSaved)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        var planetSaved = this.testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = this.planetRepository.findById(planetSaved.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        Optional<Planet> sut = this.planetRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        var planetSaved = this.testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = this.planetRepository.findByName(planetSaved.getName());

        assertThat(sut).isNotNull();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty() {
        Optional<Planet> sut = this.planetRepository.findByName("Name");

        assertThat(sut).isEmpty();
    }

    @Sql(scripts = {"/insert_planets.sql"})
    @Test
    public void listPlanets_ReturnsFilteredPlanets() {
        List<Planet> responseWithoutArgs = this.planetRepository.findAll(QueryBuilder.build(new Planet()));
        List<Planet> responseWithArgs = this.planetRepository.findAll(QueryBuilder.build(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain())));

        assertThat(responseWithoutArgs).isNotEmpty();
        assertThat(responseWithoutArgs).hasSize(3);
        assertThat(responseWithArgs).isNotEmpty();
        assertThat(responseWithArgs).hasSize(1);
        assertThat(responseWithArgs.getFirst()).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        List<Planet> sut = this.planetRepository.findAll(QueryBuilder.build(new Planet()));

        assertThat(sut).isEmpty();
        assertThat(sut).hasSize(0);
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
        var planetSaved = this.testEntityManager.persistFlushFind(PLANET);

        this.planetRepository.deleteById(planetSaved.getId());

        var sut = this.testEntityManager.find(Planet.class, planetSaved.getId());

        assertThat(sut).isNull();
    }
}
