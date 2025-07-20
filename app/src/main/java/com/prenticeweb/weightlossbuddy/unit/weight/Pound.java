package com.prenticeweb.weightlossbuddy.unit.weight;



import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;

public class Pound extends Unit {

    private static final int DEFAULT_SCALE = 1;
    private static final String UNIT_SHORTHAND = "lbs";

    public Pound(BigDecimal quantity) {
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
