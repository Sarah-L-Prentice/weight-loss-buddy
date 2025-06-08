package com.prenticeweb.weightlossbuddy.unit;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Unit {

    protected RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private final BigDecimal quantity;

    public Unit(BigDecimal quantity){
        this.quantity = quantity;
    }

    protected abstract String getUnitNameShorthand();
    protected abstract int getDefaultScale();

    public String getFormattedUnit(int scale) {
        return StringUtils.join(quantity.setScale(scale, DEFAULT_ROUNDING_MODE), getUnitNameShorthand());
    }

    public String getFormattedUnit(){
        return getFormattedUnit(getDefaultScale());
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
