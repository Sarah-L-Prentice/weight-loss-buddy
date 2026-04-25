package com.prenticeweb.weightlossbuddy.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

class CompoundUnitTest {

    @ParameterizedTest
    @CsvSource({
        // Positive minus positive
        "5, 3, 2, 1, 3st 2.0lbs",
        "10, 8, 3, 2, 7st 6.0lbs",
        "2, 10, 1, 5, 1st 5.0lbs",
        "5, 0, 2, 8, 2st 6.0lbs",
        "12, 6, 12, 0, 0st 6.0lbs",

        // Decimal places
        "12, 6.2, 11, 13.6, 0st 6.6lbs",

        // Zero cases
        "0, 0, 0, 0, 0st 0.0lbs",
        "5, 0, 5, 0, 0st 0.0lbs",
        "0, 5, 0, 5, 0st 0.0lbs",
        
        // Borrowing cases (when minor units need to borrow)
        "5, 2, 2, 8, 2st 8.0lbs",
        "10, 3, 5, 10, 4st 7.0lbs",
        "3, 1, 1, 13, 1st 2.0lbs",
        "11, 13, 12, 6, -0st 7.0lbs"
    })
    void subtractWithVariousCombinations(BigDecimal stone1, BigDecimal lb1, 
                                        BigDecimal stone2, BigDecimal lb2, 
                                        String expected) {
        StoneAndPounds first = new StoneAndPounds(stone1, lb1, stone1.compareTo(BigDecimal.ZERO) < 0);
        StoneAndPounds second = new StoneAndPounds(stone2, lb2, stone2.compareTo(BigDecimal.ZERO) < 0);
        
        StoneAndPounds result = first.subtract(second);
        assertThat(result.getFormattedUnit()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        // Edge cases with zero
        "0, 5, 0, 3, 0st 2.0lbs",
        "0, 3, 0, 5, -0st 2.0lbs",
        "0, 0, 5, 0, -5st 0.0lbs",
        "5, 0, 0, 0, 5st 0.0lbs",
        
        // Large values
        "100, 10, 50, 5, 50st 5.0lbs",
        "-100, 10, 50, 5, -151st 1.0lbs",
        "100, 10, -50, 5, 151st 1.0lbs",
        
        // Decimal values in minor units
        "5, 7.5, 2, 3.2, 3st 4.3lbs",
        "10, 8.8, 5, 4.4, 5st 4.4lbs"
    })
    void subtractEdgeCases(BigDecimal stone1, BigDecimal lb1, 
                          BigDecimal stone2, BigDecimal lb2, 
                          String expected) {
        StoneAndPounds first = new StoneAndPounds(stone1, lb1, stone1.compareTo(BigDecimal.ZERO) < 0);
        StoneAndPounds second = new StoneAndPounds(stone2, lb2, stone2.compareTo(BigDecimal.ZERO) < 0);
        
        StoneAndPounds result = first.subtract(second);
        assertThat(result.getFormattedUnit()).isEqualTo(expected);
    }

    @Test
    void subtractSameUnitsReturnsZero() {
        StoneAndPounds first = new StoneAndPounds(new BigDecimal("5"), new BigDecimal("3"), false);
        StoneAndPounds second = new StoneAndPounds(new BigDecimal("5"), new BigDecimal("3"), false);
        
        StoneAndPounds result = first.subtract(second);
        assertThat(result.getFormattedUnit()).isEqualTo("0st 0.0lbs");
    }

    @Test
    void subtractNegativeFromPositive() {
        StoneAndPounds positive = new StoneAndPounds(new BigDecimal("10"), new BigDecimal("5"), false);
        StoneAndPounds negative = new StoneAndPounds(new BigDecimal("3"), new BigDecimal("2"), true);
        
        StoneAndPounds result = positive.subtract(negative);
        assertThat(result.getFormattedUnit()).isEqualTo("13st 7.0lbs");
    }

    @Test
    void subtractPositiveFromNegative() {
        StoneAndPounds negative = new StoneAndPounds(new BigDecimal("10"), new BigDecimal("5"), true);
        StoneAndPounds positive = new StoneAndPounds(new BigDecimal("3"), new BigDecimal("2"), false);
        
        StoneAndPounds result = negative.subtract(positive);
        assertThat(result.getFormattedUnit()).isEqualTo("-13st 7.0lbs");
    }
}
