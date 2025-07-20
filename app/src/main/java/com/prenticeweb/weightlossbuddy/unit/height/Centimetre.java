package com.prenticeweb.weightlossbuddy.unit.height;

import com.prenticeweb.weightlossbuddy.unit.Unit;

import java.math.BigDecimal;

public class Centimetre extends Unit {

    public Centimetre(BigDecimal quantity) {
        super(quantity);
    }

    @Override
    protected String getUnitNameShorthand() {
        return "cm";
    }

    @Override
    protected int getDefaultScale() {
        return 0;
    }
}
