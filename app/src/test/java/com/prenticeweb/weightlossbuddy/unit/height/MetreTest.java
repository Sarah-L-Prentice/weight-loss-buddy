package com.prenticeweb.weightlossbuddy.unit.height;

import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.UnitBaseTest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

public class MetreTest extends UnitBaseTest {

    @Override
    public String getExpectedShorthandUnit() {
        return "m";
    }

    @Override
    public Unit getUnitToTest(BigDecimal amount) {
        return new Metre(amount);
    }

    @Override
    @CsvSource({
            "76.430897 , 76.43",
            "76.43     , 76.43",
            "120.434567, 120.43",
            "120.198567, 120.20",
            "120.998567, 121.00",
            "167.125567, 167.13",
            "167       , 167.00",
    })
    protected void getFormattedUnit(BigDecimal amount, String expectedReturnVal) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getFormattedUnit()).isEqualTo(expectedReturnVal + getExpectedShorthandUnit());
    }

    @Override
    @ParameterizedTest
    @CsvSource({
            "-10.230123, -10.23",
            " 10.239876, +10.24"
    })
    protected void getSignedFormattedUnit(BigDecimal amount, String expected) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getSignedFormattedUnit()).isEqualTo(expected + getExpectedShorthandUnit());
    }

}
