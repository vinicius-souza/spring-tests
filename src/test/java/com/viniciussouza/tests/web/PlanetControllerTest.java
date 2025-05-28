package com.viniciussouza.tests.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viniciussouza.tests.domain.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static com.viniciussouza.tests.common.PlanetConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//Anotação do Spring para utilização de requisições HTTP.
//Restringindo o carregamento apenas do PlanetController
@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanetService planetService;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(
                        post("/planets")
                                .content(objectMapper.writeValueAsString(PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET)); //$ indica a raíz do objeto JSON
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {

        mockMvc.perform(
                        post("/planets")
                                .content(objectMapper.writeValueAsString(INVALID_PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(
                        post("/planets")
                                .content(objectMapper.writeValueAsString(EMPTY_PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(
                        post("/planets")
                                .content(objectMapper.writeValueAsString(PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        when(planetService.get(1L)).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                        get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {

        mockMvc.perform(
                        get("/planets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetService.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                        get("/planets/name/Name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception {

        mockMvc.perform(
                        get("/planets/name/Name"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATOOINE.getClimate(), TATOOINE.getTerrain())).thenReturn(List.of(TATOOINE));

        mockMvc.perform(
                        get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(
                        get("/planets?" + String.format("climate=%s&terrain=%s", TATOOINE.getClimate(), TATOOINE.getTerrain())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(TATOOINE));
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.list(any(), any())).thenReturn(List.of());

        mockMvc.perform(
                        get("/planets?climate=Climate&terrain=Terrain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(
                        delete("/planets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_WithUnexistentId_ReturnsNotFound() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(planetService).remove(1L);
        mockMvc.perform(
                delete("/planets/1"))
                .andExpect(status().isNotFound());
    }
}
