package com.prenticeweb.weightlossbuddy.unit.height;

import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;

public class Inch extends Unit {

    public Inch(BigDecimal quantity) {
        super(quantity);
    }

    @Override
    protected String getUnitNameShorthand() {
        return "\"";
    }

    @Override
    protected int getDefaultScale() {
        return 0;
    }
}
