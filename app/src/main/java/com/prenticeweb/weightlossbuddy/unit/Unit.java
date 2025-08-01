package com.prenticeweb.weightlossbuddy.unit;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Unit {

    protected RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private final BigDecimal quantity;

    public Unit(BigDecimal quantity) {
        this.quantity = quantity;
    }

    protected abstract String getUnitNameShorthand();

    protected abstract int getDefaultScale();

    public String getFormattedUnit(int scale) {
        return StringUtils.join(getScaledUnit(scale), getUnitNameShorthand());
    }

    public String getFormattedUnit() {
        return getFormattedUnit(getDefaultScale());
    }

    public String getSignedFormattedUnit(int scale) {
        String sign = quantity.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
        return sign + getFormattedUnit(scale);
    }

    public String getSignedFormattedUnit() {
        return getSignedFormattedUnit(getDefaultScale());
    }

    public String getScaledUnit() {
        return quantity.setScale(getDefaultScale(), DEFAULT_ROUNDING_MODE).toString();
    }

    public String getScaledUnit(int scale) {
        return quantity.setScale(scale, DEFAULT_ROUNDING_MODE).toString();
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public <T extends Unit> T subtract(T secondUnit) {
        try {
            BigDecimal amount = this.getQuantity().subtract(secondUnit.getQuantity());
            return (T) secondUnit.getClass().getConstructor(BigDecimal.class).newInstance(amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
