package com.prenticeweb.weightlossbuddy.unit.height;


import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.UnitBaseTest;

import java.math.BigDecimal;

public class InchTest extends UnitBaseTest {

    @Override
    public String getExpectedShorthandUnit() {
        return "\"";
    }

    @Override
    public Unit getUnitToTest(BigDecimal amount) {
        return new Inch(amount);
    }
}
