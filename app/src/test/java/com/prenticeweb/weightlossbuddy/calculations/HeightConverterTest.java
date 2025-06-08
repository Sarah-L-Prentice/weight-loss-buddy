package com.prenticeweb.weightlossbuddy.calculations;


import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.height.Centimetre;
import com.prenticeweb.weightlossbuddy.unit.height.FeetAndInches;
import com.prenticeweb.weightlossbuddy.unit.height.Inch;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

class HeightConverterTest {

    @ParameterizedTest
    @CsvSource({
            "2.2046    , 0.86795275590551",
            "4.4092    , 1.73590551181102",
            "11.0231   , 4.33980314960630",
            "17.6370   , 6.94370078740157",
            "171.9606  , 67.70102362204724",
            "178.5744  , 70.30488188976378",
            "1.1023    , 0.43397637795276",
            "1.6535    , 0.65098425196850",
            "0.5512    , 0.21700787401575",
            "0.4654    , 0.18322834645669",
            "1.9632    , 0.77291338582677",
            "182.0136  , 71.65889763779528",
            "165.0100  , 64.96456692913386",
    })
    void convertCmToInches(BigDecimal inputQuantity, BigDecimal expectedQuantity) {
        Centimetre input = new Centimetre(inputQuantity);
        Inch expected = new Inch(expectedQuantity);
        Inch result = HeightConverter.convertCmToInches(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "1       , 2.54",
            "2       , 5.08",
            "5       , 12.70",
            "8       , 20.32",
            "78      , 198.12",
            "81      , 205.74",
            "0.5     , 1.270",
            "0.75    , 1.9050",
            "0.25    , 0.6350",
            "0.2111  , 0.536194",
            "0.8905  , 2.261870",
            "82.56   , 209.7024",
            "74.84726, 190.1120404",
            "74442343.8472643244444234234, 189083553.372051384088835495436",
    })
    void convertInchesToCm(BigDecimal inputQuantity, BigDecimal expectedQuantity) {
        Inch input = new Inch(inputQuantity);
        Centimetre expected = new Centimetre(expectedQuantity);
        Centimetre result = HeightConverter.convertInchesToCm(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "1       , 0 ,   1.0000000000",
            "2       , 0 ,   2.0000000000",
            "5       , 0 ,   5.0000000000",
            "8       , 0 ,   8.0000000000",
            "78      , 6 ,   6.0000000000",
            "81      , 6 ,   9.0000000000",
            "0.5     , 0 ,   0.5000000000",
            "0.75    , 0 ,   0.7500000000",
            "0.25    , 0 ,   0.2500000000",
            "0.2111  , 0 ,   0.2111000000",
            "0.8905  , 0 ,   0.8905000000",
            "82.56   , 6 ,   10.5600000000",
            "74.8472 , 6 ,   2.8472000000",
            "65      , 5 ,   5.0000000000",
            "73      , 6 ,   1.0000000000",
            "60      , 5 ,   0.0000000000",
    })
    void convertInchesToFeetAndInches(BigDecimal inputQuantity, BigDecimal expectedFeet, BigDecimal expectedInches) {
        Inch input = new Inch(inputQuantity);
        FeetAndInches expected = new FeetAndInches(expectedFeet, expectedInches);
        FeetAndInches result = HeightConverter.convertInchesToFeetAndInches(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
        assertThat(result.getMinorUnit().getQuantity()).isEqualTo(expected.getMinorUnit().getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "1       , 0 ,   0.3937007874",
            "2       , 0 ,   0.7874015748",
            "5       , 0 ,   1.9685039370",
            "8       , 0 ,   3.1496062992",
            "78      , 2 ,   6.7086614173",
            "81      , 2 ,   7.8897637795",
            "0.5     , 0 ,   0.1968503937",
            "0.75    , 0 ,   0.2952755906",
            "0.25    , 0 ,   0.0984251969",
            "0.2111  , 0 ,   0.0831102362",
            "0.8905  , 0 ,   0.3505905512",
            "82.56   , 2 ,   8.5039370079",
            "74.8472 , 2 ,   5.4674015748",
            "165     , 5 ,   4.9606299213",
            "173     , 5 ,   8.1102362205",
            "192     , 6 ,   3.5905511811",
    })
    void convertCmToFeetAndInches(BigDecimal inputQuantity, BigDecimal expectedFeet, BigDecimal expectedInches) {
        Centimetre input = new Centimetre(inputQuantity);
        FeetAndInches expected = new FeetAndInches(expectedFeet, expectedInches);
        FeetAndInches result = HeightConverter.convertCmToFeetAndInches(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
        assertThat(result.getMinorUnit().getQuantity()).isEqualTo(expected.getMinorUnit().getQuantity());
    }

}