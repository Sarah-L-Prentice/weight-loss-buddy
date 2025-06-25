package com.prenticeweb.weightlossbuddy.unit;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class CompoundUnit<T> extends Unit {
    private Unit minorUnit;

    protected abstract Class<? extends Unit> getMinorUnitClass();

    protected CompoundUnit(BigDecimal quantityMajorUnit, BigDecimal quantityMinorUnit) {
        super(quantityMajorUnit);
        try {
            this.minorUnit = getMinorUnitClass().getConstructor(BigDecimal.class).newInstance(quantityMinorUnit);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
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
    public <T extends Unit> T subtract(T secondUnit) {
        try {
            CompoundUnit compoundUnit = (CompoundUnit) secondUnit;
            BigDecimal majorUnits = this.getQuantity().subtract(compoundUnit.getQuantity());
            BigDecimal minorUnits = this.minorUnit.getQuantity().subtract(compoundUnit.getMinorUnit().getQuantity());
            return (T) secondUnit.getClass().getConstructor(BigDecimal.class, BigDecimal.class).newInstance(majorUnits, minorUnits);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormattedUnit(int scale) {
        String minus = "-";
        if(minorUnit.getQuantity().compareTo(BigDecimal.ZERO) < 0 || getQuantity().compareTo(BigDecimal.ZERO) < 0 ) {
            return StringUtils.join(minus, getQuantity().abs(), getUnitNameShorthand(), " ", minorUnit.getFormattedUnit(scale).replace("-", ""));
        } else{
            return StringUtils.join(getQuantity(), getUnitNameShorthand(), " ", minorUnit.getFormattedUnit(scale));
        }
    }

    @Override
    public String getSignedFormattedUnit(int scale) {
        String amountString = getFormattedUnit(scale);
        if(amountString.contains("-")) {
            return amountString;
        } else {
            return "+" + amountString;
        }
    }

    public Unit getMinorUnit() {
        return minorUnit;
    }

}
