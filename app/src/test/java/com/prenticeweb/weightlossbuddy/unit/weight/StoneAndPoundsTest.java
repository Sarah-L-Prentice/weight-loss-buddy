package com.prenticeweb.weightlossbuddy.unit.weight;

import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.CompoundUnitFactory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;


class StoneAndPoundsTest {

    @ParameterizedTest
    @CsvSource({
            "2.2046, 0st 2lbs",
            "4.4092, 0st 4lbs",
            "11.0231, 0st 11lbs",
            "17.6370, 1st 4lbs",
            "171.9606, 12st 4lbs",
            "178.5744, 12st 11lbs",
            "1.1023, 0st 1lbs",
            "1.6535, 0st 2lbs",
            "0.5512, 0st 1lbs",
            "0.4654, 0st 0lbs",
            "1.9632, 0st 2lbs",
            "182.0136, 13st 0lbs",
            "165.0100, 11st 11lbs",
    })
    void defaultScaleFormatLbsToStoneWithLbs(BigDecimal lbs, String expectedReturnVal) {
        String result = getClassToTest(lbs).getFormattedUnit();
        assertThat(result).isEqualTo(expectedReturnVal);
    }

    @ParameterizedTest
    @CsvSource({
            "2.2046, 2, 0st 2.20lbs",
            "4.4092, 4, 0st 4.4092lbs",
            "11.0231, 5, 0st 11.02310lbs",
            "17.6370, 6, 1st 3.637000lbs",
            "171.9606, 7, 12st 3.9606000lbs",
            "178.5744, 8, 12st 10.57440000lbs",
            "1.1023, 9, 0st 1.102300000lbs",
            "1.6535, 10, 0st 1.6535000000lbs",
            "0.5512, 11, 0st 0.55120000000lbs",
            "0.4654, 2, 0st 0.47lbs",
            "1.9632, 3, 0st 1.963lbs",
            "182.0136, 4, 13st 0.0136lbs",
            "165.0100, 5, 11st 11.01000lbs",
    })
    void formatLbsToStoneWithLbsFormattedToInputScale(BigDecimal lbs, int scaleInput, String expectedReturnVal) {
        String result = getClassToTest(lbs).getFormattedUnit(scaleInput);
        assertThat(result).isEqualTo(expectedReturnVal);
    }

    private StoneAndPounds getClassToTest(BigDecimal lbs){
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(lbs, StoneAndPounds.class);
    }
}