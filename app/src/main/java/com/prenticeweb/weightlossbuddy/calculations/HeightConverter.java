package com.prenticeweb.weightlossbuddy.calculations;

import com.prenticeweb.weightlossbuddy.unit.CompoundUnitFactory;
import com.prenticeweb.weightlossbuddy.unit.height.Centimetre;
import com.prenticeweb.weightlossbuddy.unit.height.FeetAndInches;
import com.prenticeweb.weightlossbuddy.unit.height.Inch;
import com.prenticeweb.weightlossbuddy.unit.height.Metre;

import java.math.BigDecimal;

public class HeightConverter extends UnitConverter {

    private HeightConverter(){
        super();
    }

    private static final BigDecimal CM_IN_ONE_INCH = new BigDecimal("2.54");
    private static final BigDecimal CM_IN_ONE_METRE = new BigDecimal("100");
    private static final BigDecimal INCHES_IN_ONE_FT = new BigDecimal("12");

    public static Inch convertCmToInches(Centimetre cm) {
        return new Inch(divide(cm.getQuantity(), CM_IN_ONE_INCH));
    }

    public static Metre convertCentimetreToMetres(Centimetre cm) {
        return new Metre(divide(cm.getQuantity(), CM_IN_ONE_METRE));
    }

    public static Centimetre convertMetreToCentimetre(Metre m) {
        return new Centimetre(m.getQuantity().multiply(CM_IN_ONE_METRE));
    }

    public static Centimetre convertInchesToCm(Inch inch) {
        return new Centimetre(inch.getQuantity().multiply(CM_IN_ONE_INCH));
    }

    public static FeetAndInches convertInchesToFeetAndInches(Inch inch) {
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(inch.getQuantity(), FeetAndInches.class);
    }

    public static FeetAndInches convertCmToFeetAndInches(Centimetre cm) {
        Inch inch = convertCmToInches(cm);
        CompoundUnitFactory factory = new CompoundUnitFactory();
        return factory.newInstance(inch.getQuantity(), FeetAndInches.class);
    }

    public static Centimetre convertFeetAndInchesToCm(FeetAndInches feetAndInches) {
        Inch inches = convertFeetAndInchesToInch(feetAndInches);
        return new Centimetre(inches.getQuantity().multiply(CM_IN_ONE_INCH));
    }

    public static Inch convertFeetAndInchesToInch(FeetAndInches feetAndInches) {
        return new Inch(feetAndInches.getQuantity().multiply(INCHES_IN_ONE_FT).add(feetAndInches.getMinorUnit().getQuantity()));
    }

}