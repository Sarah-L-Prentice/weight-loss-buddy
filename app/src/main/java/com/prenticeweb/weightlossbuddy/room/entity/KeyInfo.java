package com.prenticeweb.weightlossbuddy.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class KeyInfo {

    @PrimaryKey
    private Integer identity;

    private BigDecimal heightInCm;

    private BigDecimal heightInInches;
    private BigDecimal targetWeightLb;
    private BigDecimal targetWeightKg;



    public Integer getIdentity() {
        return identity;
    }
    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public BigDecimal getHeightInCm() {
        return heightInCm;
    }

    public void setHeightInCm(BigDecimal heightInCm) {
        this.heightInCm = heightInCm;
    }

    public BigDecimal getTargetWeightLb() {
        return targetWeightLb;
    }

    public void setTargetWeightLb(BigDecimal targetWeightLb) {
        this.targetWeightLb = targetWeightLb;
    }

    public BigDecimal getTargetWeightKg() {
        return targetWeightKg;
    }

    public void setTargetWeightKg(BigDecimal targetWeightKg) {
        this.targetWeightKg = targetWeightKg;
    }

    public BigDecimal getHeightInInches() {
        return heightInInches;
    }

    public void setHeightInInches(BigDecimal heightInInches) {
        this.heightInInches = heightInInches;
    }
}
