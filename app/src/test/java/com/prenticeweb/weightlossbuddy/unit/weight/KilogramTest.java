package com.prenticeweb.weightlossbuddy.unit.weight;

import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.UnitBaseTest;

import java.math.BigDecimal;

class KilogramTest extends UnitBaseTest {

    @Override
    public String getExpectedShorthandUnit() {
        return "kg";
    }

    @Override
    public Unit getUnitToTest(BigDecimal amount) {
        return new Kilogram(amount);
    }

}