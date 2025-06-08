package com.prenticeweb.weightlossbuddy.calculations;

import static org.assertj.core.api.Assertions.assertThat;

import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

class WeightConverterTest {

    @ParameterizedTest
    @CsvSource({
            "2.2046    , 0.99998973890199",
            "4.4092    , 1.99997947780398",
            "11.0231   , 4.99999405374695",
            "17.6370   , 8.00000862968991",
            "171.9606  , 78.00001610062114",
            "178.5744  , 80.99998531732711",
            "1.1023    , 0.49999486945099",
            "1.6535    , 0.75001498379499",
            "0.5512    , 0.25002011434400",
            "0.4654    , 0.21110188899800",
            "1.9632    , 0.89049254078399",
            "182.0136  , 82.55998019623109",
            "165.0100  , 74.84727697369918",
    })
    void convertLbsToKg(BigDecimal lbInput, BigDecimal kgExpected) {
        Pound input = new Pound(lbInput);
        Kilogram expected = new Kilogram(kgExpected);
        Kilogram result = WeightConverter.convertLbToKg(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "1       , 2.2046226218488",
            "2       , 4.4092452436976",
            "5       , 11.0231131092440",
            "8       , 17.6369809747904",
            "78      , 171.9605645042064",
            "81      , 178.5744323697528",
            "0.5     , 1.10231131092440",
            "0.75    , 1.653466966386600",
            "0.25    , 0.551155655462200",
            "0.2111  , 0.46539583547228168",
            "0.8905  , 1.96321644475635640",
            "82.56   , 182.013643659836928",
            "74.84726, 165.009962579398814288",
    })
    void convertKgToLbs(BigDecimal kgInput, BigDecimal lbExpected) {
        Kilogram kg = new Kilogram(kgInput);
        Pound result = WeightConverter.convertKgToLb(kg);
        Pound expected = new Pound(lbExpected);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "1       , 0 ,   2.2046226218",
            "2       , 0 ,   4.4092452437",
            "5       , 0 ,   11.0231131092",
            "8       , 1 ,   3.6369809748",
            "78      , 12,   3.9605645042",
            "81      , 12,   10.5744323698",
            "0.5     , 0 ,   1.1023113109",
            "0.75    , 0 ,   1.6534669664",
            "0.25    , 0 ,   0.5511556555",
            "0.2111  , 0 ,   0.4653958355",
            "0.8905  , 0 ,   1.9632164448",
            "82.56   , 13,   0.0136436598",
            "74.84726, 11,   11.0099625794",
    })
    void convertKgToStoneAndPounds(BigDecimal inputQuantity, BigDecimal expectedStone, BigDecimal expectedPounds) {
        Kilogram input = new Kilogram(inputQuantity);
        StoneAndPounds expected = new StoneAndPounds(expectedStone, expectedPounds);
        StoneAndPounds result = WeightConverter.convertKgToStoneAndPounds(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
        assertThat(result.getMinorUnit().getQuantity()).isEqualTo(expected.getMinorUnit().getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "1       , 0 ,   1.0000000000",
            "2       , 0 ,   2.0000000000",
            "5       , 0 ,   5.0000000000",
            "8       , 0 ,   8.0000000000",
            "78      , 5 ,   8.0000000000",
            "81      , 5 ,   11.0000000000",
            "0.5     , 0 ,   0.5000000000",
            "0.75    , 0 ,   0.7500000000",
            "0.25    , 0 ,   0.2500000000",
            "0.2111  , 0 ,   0.2111000000",
            "0.8905  , 0 ,   0.8905000000",
            "82.56   , 5 ,   12.5600000000",
            "74.84726, 5 ,   4.8472600000",
    })
    void convertPoundsToStoneAndPounds(BigDecimal inputQuantity, BigDecimal expectedStone, BigDecimal expectedPounds) {
        Pound input = new Pound(inputQuantity);
        StoneAndPounds expected = new StoneAndPounds(expectedStone, expectedPounds);
        StoneAndPounds result = WeightConverter.convertPoundsToStoneAndPounds(input);
        assertThat(result.getQuantity()).isEqualTo(expected.getQuantity());
        assertThat(result.getMinorUnit().getQuantity()).isEqualTo(expected.getMinorUnit().getQuantity());
    }
}