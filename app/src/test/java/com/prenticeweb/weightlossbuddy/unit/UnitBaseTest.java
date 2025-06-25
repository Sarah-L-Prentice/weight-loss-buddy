package com.prenticeweb.weightlossbuddy.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;


public abstract class UnitBaseTest {


    public abstract String getExpectedShorthandUnit();

    public abstract Unit getUnitToTest(BigDecimal amount);

    @ParameterizedTest
    @CsvSource({
            "76.430897 , 76.43",
            "76.43     , 76.43",
            "120.434567, 120.43",
            "120.198567, 120.20",
            "120.998567, 121.00",
            "167.125567, 167.13",
            "167       , 167.00",
    })
    void getFormattedUnit(BigDecimal amount, String expectedReturnVal) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getFormattedUnit()).isEqualTo(expectedReturnVal + getExpectedShorthandUnit());
    }

    @ParameterizedTest
    @CsvSource({
            "76.5      , 0, 77",
            "76.4      , 0, 76",
            "76.4      , 1, 76.4",
            "76.43     , 2, 76.43",
            "120.434567, 3, 120.435",
            "120.198567, 4, 120.1986",
            "120.998567, 5, 120.99857",
            "167.125567, 6, 167.125567",
            "167       , 7, 167.0000000",
    })
    void getFormattedUnitSetToScale(BigDecimal amount, int scale, String expectedReturnVal) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getFormattedUnit(scale)).isEqualTo(expectedReturnVal + getExpectedShorthandUnit());
    }

    @ParameterizedTest
    @CsvSource({
            "-10.230123, -10.23",
            " 10.239876, +10.24"
    })
    void getSignedFormattedUnit(BigDecimal amount, String expected) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getSignedFormattedUnit()).isEqualTo(expected + getExpectedShorthandUnit());
    }

    @ParameterizedTest
    @CsvSource({
            "-10.230123, 2, -10.23",
            "-10.230123, 4, -10.2301",
            " 10.239876, 2, +10.24",
            " 10.239876, 4, +10.2399"
    })
    void getSignedFormattedUnit(BigDecimal amount, int scale, String expected) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getSignedFormattedUnit(scale)).isEqualTo(expected + getExpectedShorthandUnit());
    }
}
