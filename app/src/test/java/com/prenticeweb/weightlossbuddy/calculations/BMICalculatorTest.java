package com.prenticeweb.weightlossbuddy.calculations;

import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.height.Metre;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

class BMICalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "70, 1.75, 22.9",
            "73, 1.7 , 25.3"
    })
    void calculateBMI(BigDecimal kg, BigDecimal metres, BigDecimal expectedBMI) {
        BigDecimal result = BMICalculator.calculateBMI(new Kilogram(kg), new Metre(metres));
        assertThat(result).isEqualTo(expectedBMI);
    }

}