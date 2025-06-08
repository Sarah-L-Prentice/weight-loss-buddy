package com.prenticeweb.weightlossbuddy.unit.weight;

import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.UnitBaseTest;

import java.math.BigDecimal;

class PoundTest extends UnitBaseTest {

    @Override
    public String getExpectedShorthandUnit() {
        return "lbs";
    }

    @Override
    public Unit getUnitToTest(BigDecimal amount) {
        return new Pound(amount);
    }
}