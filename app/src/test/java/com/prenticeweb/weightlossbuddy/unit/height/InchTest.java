package com.prenticeweb.weightlossbuddy.unit.height;


import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.UnitBaseTest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

public class InchTest extends UnitBaseTest {

    @Override
    public String getExpectedShorthandUnit() {
        return "\"";
    }

    @Override
    public Unit getUnitToTest(BigDecimal amount) {
        return new Inch(amount);
    }

    @Override
    @ParameterizedTest
    @CsvSource({
            "-10.230123, -10",
            " 10.239876, +10"
    })
    protected void getSignedFormattedUnit(BigDecimal amount, String expected) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getSignedFormattedUnit()).isEqualTo(expected + getExpectedShorthandUnit());
    }

    @Override
    @CsvSource({
            "76.430897 , 76",
            "76.43     , 76",
            "120.434567, 120",
            "120.198567, 120",
            "120.998567, 121",
            "167.125567, 167",
            "167       , 167",
    })
    protected void getFormattedUnit(BigDecimal amount, String expectedReturnVal) {
        Unit classToTest = getUnitToTest(amount);
        assertThat(classToTest.getFormattedUnit()).isEqualTo(expectedReturnVal + getExpectedShorthandUnit());
    }
}
