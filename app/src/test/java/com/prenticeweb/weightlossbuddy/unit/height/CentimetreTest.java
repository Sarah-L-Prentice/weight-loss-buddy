package com.prenticeweb.weightlossbuddy.unit.height;



import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.UnitBaseTest;

import java.math.BigDecimal;

public class CentimetreTest extends UnitBaseTest {

    @Override
    public String getExpectedShorthandUnit() {
        return "cm";
    }

    @Override
    public Unit getUnitToTest(BigDecimal amount) {
        return new Centimetre(amount);
    }
}
