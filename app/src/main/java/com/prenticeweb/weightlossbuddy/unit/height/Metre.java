package com.prenticeweb.weightlossbuddy.unit.height;

import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;

public class Metre extends Unit {
    public Metre(BigDecimal quantity) {
        super(quantity);
    }

    @Override
    protected String getUnitNameShorthand() {
        return "m";
    }

    @Override
    protected int getDefaultScale() {
        return 2;
    }
}
