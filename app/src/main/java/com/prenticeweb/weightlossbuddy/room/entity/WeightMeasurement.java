package com.prenticeweb.weightlossbuddy.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}
