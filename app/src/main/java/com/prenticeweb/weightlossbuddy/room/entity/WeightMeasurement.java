package com.prenticeweb.weightlossbuddy.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.prenticeweb.weightlossbuddy.calculations.WeightConverter;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class WeightMeasurement {

    @PrimaryKey
    private Date date;
    private BigDecimal weightKg;
    private BigDecimal weightLb;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getWeightLb() {
        return weightLb;
    }

    public void setWeightLb(BigDecimal weightLb) {
        this.weightLb = weightLb;
    }

    /**
     * Subtracts another WeightMeasurement from this one and returns a new WeightMeasurement
     * representing the difference. Uses WeightConverter for calculations.
     * 
     * @param other The WeightMeasurement to subtract from this one
     * @return A new WeightMeasurement representing the difference (this - other)
     */
    public WeightMeasurement subtract(WeightMeasurement other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot subtract null WeightMeasurement");
        }

        // Convert both weights to kilograms for accurate subtraction
        Kilogram thisKg = new Kilogram(this.weightKg);
        Kilogram otherKg = new Kilogram(other.weightKg);
        
        // Calculate the difference in kilograms
        BigDecimal differenceKg = thisKg.getQuantity().subtract(otherKg.getQuantity());
        
        // Convert the difference back to pounds using WeightConverter
        Pound differenceLb = WeightConverter.convertKgToLb(new Kilogram(differenceKg));
        
        // Create and return a new WeightMeasurement with the difference
        WeightMeasurement result = new WeightMeasurement();
        result.setWeightKg(differenceKg);
        result.setWeightLb(differenceLb.getQuantity());
        // Note: date is not set for the difference measurement as it represents a delta
        
        return result;
    }

}
