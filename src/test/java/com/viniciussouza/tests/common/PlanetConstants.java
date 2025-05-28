package com.viniciussouza.tests.common;

import com.viniciussouza.tests.domain.Planet;

import java.util.List;

public class PlanetConstants {
    public static final Planet EMPTY_PLANET = new Planet();
    public static final Planet PLANET = new Planet("Name", "Climate", "Terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");



    public static final Planet TATOOINE = new Planet(1L,"Tatooine", "Arid", "Desert");
    public static final Planet ALDERAAN = new Planet(2L,"Alderaan", "Temperate", "Grasslands, Mountains");
    public static final Planet YAVINIV = new Planet(3L,"Yavin IV", "Temperate, Tropical", "Jungle, Rainforest");

    public static final List<Planet> PLANETS = List.of(TATOOINE, ALDERAAN, YAVINIV);
}
