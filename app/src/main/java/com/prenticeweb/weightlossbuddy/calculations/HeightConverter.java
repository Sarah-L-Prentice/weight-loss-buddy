package com.prenticeweb.weightlossbuddy.calculations;

import com.prenticeweb.weightlossbuddy.unit.CompoundUnitFactory;
import com.prenticeweb.weightlossbuddy.unit.height.Centimetre;
import com.prenticeweb.weightlossbuddy.unit.height.FeetAndInches;
import com.prenticeweb.weightlossbuddy.unit.height.Inch;

import java.math.BigDecimal;

public class HeightConverter extends UnitConverter {

    private HeightConverter(){
        super();
    }

    private static final BigDecimal CM_IN_ONE_INCH = new BigDecimal("2.54");

    public static final Inch convertCmToInches(Centimetre cm) {
        return new Inch(divide(cm.getQuantity(), CM_IN_ONE_INCH));
    }

    public static final Centimetre convertInchesToCm(Inch inch) {
        return new Centimetre(inch.getQuantity().multiply(CM_IN_ONE_INCH));
    }

    public static final FeetAndInches convertInchesToFeetAndInches(Inch inch) {
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(inch.getQuantity(), FeetAndInches.class);
    }

    public static final FeetAndInches convertCmToFeetAndInches(Centimetre cm) {
        Inch inch = convertCmToInches(cm);
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(inch.getQuantity(), FeetAndInches.class);
    }

}