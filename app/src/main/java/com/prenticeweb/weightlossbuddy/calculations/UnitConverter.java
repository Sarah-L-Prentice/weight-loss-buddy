package com.prenticeweb.weightlossbuddy.calculations;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class UnitConverter {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    protected static BigDecimal divide(BigDecimal amount, BigDecimal divisor) {
        return amount.divide(divisor, 14, ROUNDING_MODE);
    }

}