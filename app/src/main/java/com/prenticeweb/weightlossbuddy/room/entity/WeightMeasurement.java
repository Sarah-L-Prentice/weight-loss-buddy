package com.prenticeweb.weightlossbuddy.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
public class WeightMeasurement {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private Date date;
    private BigDecimal weightKg;
    private BigDecimal weightLb;
    private BigDecimal weightStonesAndLb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public BigDecimal getWeightStonesAndLb() {
        return weightStonesAndLb;
    }

    public void setWeightStonesAndLb(BigDecimal weightStonesAndLb) {
        this.weightStonesAndLb = weightStonesAndLb;
    }
}
