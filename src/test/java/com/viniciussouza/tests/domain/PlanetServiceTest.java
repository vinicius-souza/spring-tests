package com.viniciussouza.tests.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static com.viniciussouza.tests.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;

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
}
