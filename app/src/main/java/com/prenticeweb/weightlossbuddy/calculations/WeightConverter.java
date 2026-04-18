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
    private static final BigDecimal LBS_IN_ONE_STONE = new BigDecimal("14");

    public static Pound convertKgToLb(Kilogram kg) {
        return new Pound(kg.getQuantity().multiply(LBS_IN_ONE_KG));
    }

    public static Kilogram convertLbToKg(Pound lb) {
        return new Kilogram(divide(lb.getQuantity(), LBS_IN_ONE_KG));
    }

    public static StoneAndPounds convertKgToStoneAndPounds(Kilogram kg) {
        Pound lb = convertKgToLb(kg);
        return convertPoundsToStoneAndPounds(lb);
    }

    public static StoneAndPounds convertPoundsToStoneAndPounds(Pound lb) {
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(lb.getQuantity(), StoneAndPounds.class, lb.getQuantity().compareTo(BigDecimal.ZERO) < 0);
    }

    public static Pound convertStoneAndPoundsToPounds(StoneAndPounds stoneAndPounds) {
        return new Pound(stoneAndPounds.getQuantity().multiply(LBS_IN_ONE_STONE).add(stoneAndPounds.getMinorUnit().getQuantity()));
    }


}