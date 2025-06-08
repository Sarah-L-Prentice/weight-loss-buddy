package com.prenticeweb.weightlossbuddy.unit.height;


import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.CompoundUnitFactory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;


class FeetAndInchesTest {

    @ParameterizedTest
    @CsvSource({
            "2.2046, 0ft 2\"",
            "4.4092, 0ft 4\"",
            "11.0231, 0ft 11\"",
            "17.6370, 1ft 6\"",
            "171.9606, 14ft 4\"",
            "178.5744, 14ft 11\"",
            "1.1023, 0ft 1\"",
            "1.6535, 0ft 2\"",
            "0.5512, 0ft 1\"",
            "0.4654, 0ft 0\"",
            "1.9632, 0ft 2\"",
            "182.0136, 15ft 2\"",
            "165.0100, 13ft 9\"",
    })
    public void defaultScaleFormatFeetAndInches(BigDecimal inches, String expectedReturnVal) {
        String result = getClassToTest(inches).getFormattedUnit();
        assertThat(result).isEqualTo(expectedReturnVal);
    }

    @ParameterizedTest
    @CsvSource({
            "2.2046, 2, 0ft 2.20\"",
            "4.4092, 4, 0ft 4.4092\"",
            "11.0231, 5, 0ft 11.02310\"",
            "17.6370, 6, 1ft 5.637000\"",
            "171.9606, 7, 14ft 3.9606000\"",
            "178.5744, 8, 14ft 10.57440000\"",
            "1.1023, 9, 0ft 1.102300000\"",
            "1.6535, 10, 0ft 1.6535000000\"",
            "0.5512, 11, 0ft 0.55120000000\"",
            "0.4654, 2, 0ft 0.47\"",
            "1.9632, 3, 0ft 1.963\"",
            "182.0136, 4, 15ft 2.0136\"",
            "165.0100, 5, 13ft 9.01000\"",
    })
    public void feetAndInchesFormattedToInputScale(BigDecimal inches, int scaleInput, String expectedReturnVal) {
        String result = getClassToTest(inches).getFormattedUnit(scaleInput);
        assertThat(result).isEqualTo(expectedReturnVal);
    }

    private FeetAndInches getClassToTest(BigDecimal inches) {
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(inches, FeetAndInches.class);
    }
}