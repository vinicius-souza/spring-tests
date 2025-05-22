package com.viniciussouza.tests.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static com.viniciussouza.tests.common.PlanetConstants.PLANET;
import static com.viniciussouza.tests.common.PlanetConstants.INVALID_PLANET;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    public PlanetServiceTest() {
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        when(this.planetRepository.save(PLANET)).thenReturn(PLANET);
        //SUT = System Under Test (Indica qual variável está sendo alvo do teste em questão)
        var sut = this.planetService.create(PLANET);

        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(this.planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> this.planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getById_WithValidId_ReturnsPlanet() {
        when(this.planetRepository.findById(PLANET.getId())).thenReturn(java.util.Optional.of(PLANET));

        var sut = this.planetService.get(PLANET.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getById_WithInvalidId_ReturnsEmpty() {
        when(this.planetRepository.findById(-1L)).thenReturn(Optional.empty());

        var sut = this.planetService.get(-1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getByName_WithValidName_ReturnsPlanet() {
        when(this.planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        var sut = this.planetService.getByName(PLANET.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getByName_WithInvalidName_ReturnsEmpty() {
        when(this.planetRepository.findByName("")).thenReturn(Optional.empty());

        var sut = this.planetService.getByName("");

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanetList_ReturnsAllPlanets() {
        var query = QueryBuilder.build(new Planet(PLANET.getTerrain(), PLANET.getClimate()));
        when(this.planetRepository.findAll(query)).thenReturn(List.of(PLANET));

        var sut = this.planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(List.of(PLANET).size());
        assertThat(sut.getFirst()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanetList_ReturnsNoPlanets() {
        when(this.planetRepository.findAll(any())).thenReturn(List.of());

        var sut = this.planetService.list("", "");

        assertThat(sut).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_NoExceptionIsThrown() {
        //Não é necessário setup com Stub porque o remove não retorna nada
        assertThatCode(() ->
            planetService.remove(PLANET.getId())
        ).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() {
        //doThrow deve ser usado porque deleteById não tem retorno, logo não se pode usar o thenThrow
       doThrow(RuntimeException.class).when(planetRepository).deleteById(-1L);
       assertThatThrownBy(() -> planetService.remove(-1L)).isInstanceOf(RuntimeException.class);
    }
}