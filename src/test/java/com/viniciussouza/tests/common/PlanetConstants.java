package com.viniciussouza.tests.common;

import com.viniciussouza.tests.domain.Planet;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("Name", "Terrain", "Climate");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
}
