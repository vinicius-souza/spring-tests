package com.viniciussouza.tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalculatorTest {

	@Test
	void testSum() {
		var calculator = new Calculator();
		assertThat(calculator.sum(1,1)).isEqualTo(2);
	}

}
