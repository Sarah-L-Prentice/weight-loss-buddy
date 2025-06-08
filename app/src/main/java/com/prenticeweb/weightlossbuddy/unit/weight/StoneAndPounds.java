package com.prenticeweb.weightlossbuddy.unit.weight;

import com.prenticeweb.weightlossbuddy.unit.CompoundUnit;
import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;

public class StoneAndPounds extends CompoundUnit {

    private static final int DEFAULT_SCALE_STONE = 0;
    private static final int DEFAULT_SCALE_LB = 0;
    private static final String STONE_SHORTHAND = "st";
    private static final BigDecimal LBS_IN_ONE_STONE = new BigDecimal("14");

    public StoneAndPounds(BigDecimal quantityMajorUnit, BigDecimal quantityMinorUnit) {
        super(quantityMajorUnit, quantityMinorUnit);
    }

    @Override
    public String getUnitNameShorthand() {
        return STONE_SHORTHAND;
    }

    @Override
    protected int getDefaultScale() {
        return DEFAULT_SCALE_STONE;
    }

    @Override
    protected Class<? extends Unit> getMinorUnitClass() {
        return Pound.class;
    }

    @Override
    protected int getDefaultScaleMinorUnit() {
        return DEFAULT_SCALE_LB;
    }

    @Override
    protected BigDecimal getQuantityMinorUnitsInMajorUnits() {
        return LBS_IN_ONE_STONE;
    }
}
