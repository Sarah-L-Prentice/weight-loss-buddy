package com.prenticeweb.weightlossbuddy.calculations;

import com.prenticeweb.weightlossbuddy.unit.height.Metre;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BMICalculator {

    private BMICalculator(){
        //Utility class
    }

    public static BigDecimal calculateBMI(Kilogram weight, Metre height) {
        BigDecimal heightSquared = height.getQuantity().multiply(height.getQuantity());
        return weight.getQuantity().divide(heightSquared, 1, RoundingMode.HALF_UP);
    }
}
