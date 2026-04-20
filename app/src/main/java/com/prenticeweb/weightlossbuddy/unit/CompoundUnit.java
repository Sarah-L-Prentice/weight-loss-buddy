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

            BigDecimal totalMinorUnitsFirst = negateQuantity().multiply(quantityMinorUnitsInMajorUnits).add(negateMinorUnitQuantity());
            BigDecimal totalMinorUnitsSecond = compoundUnit.negateQuantity().multiply(quantityMinorUnitsInMajorUnits).add(compoundUnit.negateMinorUnitQuantity());

            BigDecimal resultMinorUnits = totalMinorUnitsFirst.subtract(totalMinorUnitsSecond);
            boolean isNegative = resultMinorUnits.compareTo(BigDecimal.ZERO) < 0;
            BigDecimal majorUnits = resultMinorUnits.abs().divide(quantityMinorUnitsInMajorUnits, 0, RoundingMode.FLOOR);
            BigDecimal minorUnits = resultMinorUnits.abs().subtract(majorUnits.multiply(quantityMinorUnitsInMajorUnits));

            T result = (T) secondUnit.getClass().getConstructor(BigDecimal.class, BigDecimal.class, boolean.class).newInstance(majorUnits, minorUnits, isNegative);
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
