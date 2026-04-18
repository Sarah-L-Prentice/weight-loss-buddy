package com.prenticeweb.weightlossbuddy.unit;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

public class CompoundUnitFactory {

    public <T extends CompoundUnit> T newInstance(BigDecimal totalMinorUnit, Class<? extends CompoundUnit> clazz, boolean isNegative) {
        try {
            CompoundUnit cp = clazz.getConstructor(BigDecimal.class, BigDecimal.class, boolean.class).newInstance(BigDecimal.ZERO, BigDecimal.ZERO, isNegative);
            BigDecimal minorUnitQuantity = cp.calculateMinorUnitQuantity(totalMinorUnit.abs());
            BigDecimal majorUnitQuantity = cp.calculateMajorUnitQuantity(totalMinorUnit.abs());
            return (T) clazz.getConstructor(BigDecimal.class, BigDecimal.class, boolean.class).newInstance(majorUnitQuantity, minorUnitQuantity, isNegative);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    public <T extends CompoundUnit> T newInstance(BigDecimal totalMinorUnit, Class<? extends CompoundUnit> clazz) {
        try {
            CompoundUnit cp = clazz.getConstructor(BigDecimal.class, BigDecimal.class).newInstance(BigDecimal.ZERO, BigDecimal.ZERO);
            BigDecimal minorUnitQuantity = cp.calculateMinorUnitQuantity(totalMinorUnit.abs());
            BigDecimal majorUnitQuantity = cp.calculateMajorUnitQuantity(totalMinorUnit.abs());
            return (T) clazz.getConstructor(BigDecimal.class, BigDecimal.class).newInstance(majorUnitQuantity, minorUnitQuantity);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
