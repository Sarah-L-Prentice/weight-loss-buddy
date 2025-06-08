package com.prenticeweb.weightlossbuddy.unit.height;



import com.prenticeweb.weightlossbuddy.unit.CompoundUnit;
import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;

public class FeetAndInches extends CompoundUnit {
    
    private static final int DEFAULT_SCALE_FEET = 0;
    private static final int DEFAULT_SCALE_INCHES = 0;
    private static final String FEET_SHORTHAND = "ft";
    private static final BigDecimal INCHES_IN_ONE_FOOT = new BigDecimal("12");

    public FeetAndInches(BigDecimal quantityMajorUnit, BigDecimal quantityMinorUnit) {
        super(quantityMajorUnit, quantityMinorUnit);
    }

    @Override
    public String getUnitNameShorthand() {
        return FEET_SHORTHAND;
    }

    @Override
    protected int getDefaultScale() {
        return DEFAULT_SCALE_FEET;
    }

    @Override
    protected Class<? extends Unit> getMinorUnitClass() {
        return Inch.class;
    }

    @Override
    protected int getDefaultScaleMinorUnit() {
        return DEFAULT_SCALE_INCHES;
    }

    @Override
    protected BigDecimal getQuantityMinorUnitsInMajorUnits() {
        return INCHES_IN_ONE_FOOT;
    }
}
