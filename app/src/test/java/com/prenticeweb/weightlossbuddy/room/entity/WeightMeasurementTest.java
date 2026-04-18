package com.prenticeweb.weightlossbuddy.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Date;

class WeightMeasurementTest {

    @ParameterizedTest
    @CsvSource({
            "80.0, 176.37, 75.0, 165.35, 5.0, 11.02311310924400",
            "100.0, 220.46, 90.0, 198.42, 10.0, 22.04622621848800",
            "50.0, 110.23, 45.0, 99.21, 5.0, 11.02311310924400",
            "70.5, 155.43, 68.2, 150.36, 2.3, 5.07063203025224",
            "120.8, 266.28, 115.3, 254.18, 5.5, 12.12542442016840"
    })
    void subtract_validMeasurements_returnsCorrectDifference(
            BigDecimal kg1, BigDecimal lb1, 
            BigDecimal kg2, BigDecimal lb2,
            BigDecimal expectedKgDiff, BigDecimal expectedLbDiff) {
        
        WeightMeasurement measurement1 = createWeightMeasurement(kg1, lb1);
        WeightMeasurement measurement2 = createWeightMeasurement(kg2, lb2);
        
        WeightMeasurement result = measurement1.subtract(measurement2);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(expectedKgDiff);
        assertThat(result.getWeightLb()).isEqualByComparingTo(expectedLbDiff);
        assertThat(result.getDate()).isNull();
    }

    @Test
    void subtract_sameWeight_returnsZero() {
        WeightMeasurement measurement = createWeightMeasurement(new BigDecimal("75.0"), new BigDecimal("165.35"));
        WeightMeasurement sameMeasurement = createWeightMeasurement(new BigDecimal("75.0"), new BigDecimal("165.35"));
        
        WeightMeasurement result = measurement.subtract(sameMeasurement);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getWeightLb()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getDate()).isNull();
    }

    @Test
    void subtract_smallerFromLarger_returnsPositiveDifference() {
        WeightMeasurement larger = createWeightMeasurement(new BigDecimal("90.0"), new BigDecimal("198.42"));
        WeightMeasurement smaller = createWeightMeasurement(new BigDecimal("70.0"), new BigDecimal("154.32"));
        
        WeightMeasurement result = larger.subtract(smaller);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(new BigDecimal("20.0"));
        assertThat(result.getWeightLb()).isEqualByComparingTo(new BigDecimal("44.09245243697600"));
        assertThat(result.getDate()).isNull();
    }

    @Test
    void subtract_largerFromSmaller_returnsNegativeDifference() {
        WeightMeasurement smaller = createWeightMeasurement(new BigDecimal("70.0"), new BigDecimal("154.32"));
        WeightMeasurement larger = createWeightMeasurement(new BigDecimal("90.0"), new BigDecimal("198.42"));
        
        WeightMeasurement result = smaller.subtract(larger);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(new BigDecimal("-20.0"));
        assertThat(result.getWeightLb()).isEqualByComparingTo(new BigDecimal("-44.09245243697600"));
        assertThat(result.getDate()).isNull();
    }

    @Test
    void subtract_withDecimalPrecision_returnsAccurateResult() {
        WeightMeasurement measurement1 = createWeightMeasurement(new BigDecimal("75.123"), new BigDecimal("165.629"));
        WeightMeasurement measurement2 = createWeightMeasurement(new BigDecimal("70.456"), new BigDecimal("155.323"));
        
        WeightMeasurement result = measurement1.subtract(measurement2);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(new BigDecimal("4.667"));
        assertThat(result.getWeightLb()).isEqualByComparingTo(new BigDecimal("10.2889737761683496"));
        assertThat(result.getDate()).isNull();
    }

    @Test
    void subtract_resultIsNewInstance_doesNotModifyOriginal() {
        WeightMeasurement original1 = createWeightMeasurement(new BigDecimal("80.0"), new BigDecimal("176.37"));
        WeightMeasurement original2 = createWeightMeasurement(new BigDecimal("70.0"), new BigDecimal("154.32"));
        
        WeightMeasurement result = original1.subtract(original2);
        
        assertThat(original1.getWeightKg()).isEqualByComparingTo(new BigDecimal("80.0"));
        assertThat(original1.getWeightLb()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(original2.getWeightKg()).isEqualByComparingTo(new BigDecimal("70.0"));
        assertThat(original2.getWeightLb()).isEqualByComparingTo(new BigDecimal("154.32"));
        assertThat(result).isNotSameAs(original1);
        assertThat(result).isNotSameAs(original2);
    }

    @Test
    void subtract_withZeroWeight_returnsCorrectDifference() {
        WeightMeasurement measurement = createWeightMeasurement(new BigDecimal("75.0"), new BigDecimal("165.35"));
        WeightMeasurement zeroWeight = createWeightMeasurement(BigDecimal.ZERO, BigDecimal.ZERO);
        
        WeightMeasurement result = measurement.subtract(zeroWeight);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(new BigDecimal("75.0"));
        assertThat(result.getWeightLb()).isEqualByComparingTo(new BigDecimal("165.34669663866000"));
        assertThat(result.getDate()).isNull();
    }

    @Test
    void subtract_zeroFromWeight_returnsCorrectDifference() {
        WeightMeasurement zeroWeight = createWeightMeasurement(BigDecimal.ZERO, BigDecimal.ZERO);
        WeightMeasurement measurement = createWeightMeasurement(new BigDecimal("75.0"), new BigDecimal("165.35"));
        
        WeightMeasurement result = zeroWeight.subtract(measurement);
        
        assertThat(result.getWeightKg()).isEqualByComparingTo(new BigDecimal("-75.0"));
        assertThat(result.getWeightLb()).isEqualByComparingTo(new BigDecimal("-165.34669663866000"));
        assertThat(result.getDate()).isNull();
    }

    private WeightMeasurement createWeightMeasurement(BigDecimal weightKg, BigDecimal weightLb) {
        WeightMeasurement measurement = new WeightMeasurement();
        measurement.setWeightKg(weightKg);
        measurement.setWeightLb(weightLb);
        measurement.setDate(new Date());
        return measurement;
    }
}
