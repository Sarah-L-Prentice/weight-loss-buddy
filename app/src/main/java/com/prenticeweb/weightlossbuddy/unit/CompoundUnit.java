package com.prenticeweb.weightlossbuddy.unit;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class CompoundUnit<T> extends Unit {

    private Unit minorUnit;

    protected abstract Class<? extends Unit> getMinorUnitClass();

    private CompoundUnit(BigDecimal quantityMajorUnit) {
        super(quantityMajorUnit);
    }

    protected CompoundUnit(BigDecimal quantityMajorUnit, BigDecimal quantityMinorUnit) {
        super(quantityMajorUnit);
        try {
            this.minorUnit = getMinorUnitClass().getConstructor(BigDecimal.class).newInstance(quantityMinorUnit);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected abstract int getDefaultScaleMinorUnit();

    protected abstract BigDecimal getQuantityMinorUnitsInMajorUnits();

    protected BigDecimal calculateMajorUnitQuantity(BigDecimal quantityMinorUnit) {
        return quantityMinorUnit.divide(getQuantityMinorUnitsInMajorUnits(), 0, RoundingMode.FLOOR);
    }

    protected BigDecimal calculateMinorUnitQuantity(BigDecimal quantityMinorUnit) {
        BigDecimal majorQuantityPart = calculateMajorUnitQuantity(quantityMinorUnit);
        return quantityMinorUnit.subtract(majorQuantityPart.multiply(getQuantityMinorUnitsInMajorUnits())).abs().setScale(10, DEFAULT_ROUNDING_MODE);
    }

    @Override
    public String getFormattedUnit(int scale) {
        return StringUtils.join(getQuantity(), getUnitNameShorthand(), " ", minorUnit.getFormattedUnit(scale));
    }

    @Override
    public String getFormattedUnit() {
        return StringUtils.join(getQuantity(), getUnitNameShorthand(), " ", minorUnit.getFormattedUnit(getDefaultScaleMinorUnit()));
    }

    public Unit getMinorUnit() {
        return minorUnit;
    }

}
