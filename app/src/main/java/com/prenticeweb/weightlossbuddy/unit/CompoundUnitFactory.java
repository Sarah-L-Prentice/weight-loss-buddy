package com.prenticeweb.weightlossbuddy.unit;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

public class CompoundUnitFactory {

    public <T extends CompoundUnit> T newInstance(BigDecimal totalMinorUnit, Class<? extends CompoundUnit> clazz) {
        try {
            CompoundUnit<? extends CompoundUnit> cp = clazz.getConstructor(BigDecimal.class, BigDecimal.class).newInstance(BigDecimal.ZERO, BigDecimal.ZERO);
            BigDecimal minorUnitQuantity = cp.calculateMinorUnitQuantity(totalMinorUnit);
            BigDecimal majorUnitQuantity = cp.calculateMajorUnitQuantity(totalMinorUnit);
            return (T) clazz.getConstructor(BigDecimal.class, BigDecimal.class).newInstance(majorUnitQuantity, minorUnitQuantity);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
