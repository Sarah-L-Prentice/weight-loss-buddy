package com.prenticeweb.weightlossbuddy.unit;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class CompoundUnit extends Unit {
    private Unit minorUnit;

    private boolean isNegative;

    protected abstract Class<? extends Unit> getMinorUnitClass();

    protected CompoundUnit(BigDecimal quantityMajorUnit, BigDecimal quantityMinorUnit, boolean isNegative) {
        super(quantityMajorUnit);
        try {
            this.minorUnit = getMinorUnitClass().getConstructor(BigDecimal.class).newInstance(quantityMinorUnit);
            this.isNegative = isNegative;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected CompoundUnit(BigDecimal quantityMajorUnit, BigDecimal quantityMinorUnit) {
        this(quantityMajorUnit, quantityMinorUnit, false);
    }

    protected abstract int getDefaultScaleMinorUnit();

    protected abstract BigDecimal getQuantityMinorUnitsInMajorUnits();

    protected BigDecimal calculateMajorUnitQuantity(BigDecimal quantityMinorUnit) {
        return quantityMinorUnit.divide(getQuantityMinorUnitsInMajorUnits(), 0, RoundingMode.DOWN);
    }

    protected BigDecimal calculateMinorUnitQuantity(BigDecimal quantityMinorUnit) {
        BigDecimal majorQuantityPart = calculateMajorUnitQuantity(quantityMinorUnit);
        return quantityMinorUnit.subtract(majorQuantityPart.multiply(getQuantityMinorUnitsInMajorUnits())).abs().setScale(10, DEFAULT_ROUNDING_MODE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Unit> T subtract(T secondUnit) {
        try {
            CompoundUnit compoundUnit = (CompoundUnit) secondUnit;
            BigDecimal quantityMinorUnitsInMajorUnits = getQuantityMinorUnitsInMajorUnits();
            BigDecimal majorUnits = negateQuantity().subtract(compoundUnit.negateQuantity());
            BigDecimal minorUnits = negateMinorUnitQuantity().subtract(compoundUnit.negateMinorUnitQuantity());

            // Handle borrowing if minor units are negative
            if (minorUnits.compareTo(BigDecimal.ZERO) < 0 && majorUnits.compareTo(BigDecimal.ZERO) > 0) {
                minorUnits = minorUnits.add(quantityMinorUnitsInMajorUnits);
                majorUnits = majorUnits.subtract(BigDecimal.ONE);
            }

            boolean isNegative = majorUnits.compareTo(BigDecimal.ZERO) < 0 || minorUnits.compareTo(BigDecimal.ZERO) < 0;
            T result = (T) secondUnit.getClass().getConstructor(BigDecimal.class, BigDecimal.class, boolean.class).newInstance(majorUnits.abs(), minorUnits.abs(), isNegative);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormattedUnit(int scale) {
        String minus = "-";
        if(minorUnit.getQuantity().compareTo(BigDecimal.ZERO) < 0 || getQuantity().compareTo(BigDecimal.ZERO) < 0) {
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

    public boolean isNegative() {
        return isNegative;
    }

    public BigDecimal negateQuantity() {
        return isNegative ? getQuantity().negate() : getQuantity();
    }

    public BigDecimal negateMinorUnitQuantity() {
        return isNegative ? getMinorUnit().getQuantity().negate() : getMinorUnit().getQuantity();
    }
}
