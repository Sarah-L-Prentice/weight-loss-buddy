package com.prenticeweb.weightlossbuddy.calculations;

import com.prenticeweb.weightlossbuddy.unit.CompoundUnitFactory;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

import java.math.BigDecimal;

public class WeightConverter extends UnitConverter {

    private WeightConverter(){
        super();
    }

    private static final BigDecimal LBS_IN_ONE_KG = new BigDecimal("2.2046226218488");

    public static final Pound convertKgToLb(Kilogram kg) {
        return new Pound(kg.getQuantity().multiply(LBS_IN_ONE_KG));
    }

    public static final Kilogram convertLbToKg(Pound lb) {
        return new Kilogram(divide(lb.getQuantity(), LBS_IN_ONE_KG));
    }

    public static final StoneAndPounds convertKgToStoneAndPounds(Kilogram kg) {
        Pound lb = convertKgToLb(kg);
        return convertPoundsToStoneAndPounds(lb);
    }

    public static final StoneAndPounds convertPoundsToStoneAndPounds(Pound lb) {
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(lb.getQuantity(), StoneAndPounds.class);
    }

}