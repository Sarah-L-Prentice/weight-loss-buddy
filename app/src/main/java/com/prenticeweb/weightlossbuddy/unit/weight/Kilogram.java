package com.prenticeweb.weightlossbuddy.unit.weight;


import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;


public class Kilogram extends Unit {

    private static final int DEFAULT_SCALE = 2;
    private static final String UNIT_SHORTHAND = "kg";

    public Kilogram(BigDecimal quantity) {
        super(quantity);
    }

    @Override
    public String getUnitNameShorthand() {
        return UNIT_SHORTHAND;
    }

    @Override
    public int getDefaultScale() {
        return DEFAULT_SCALE;
    }

}
