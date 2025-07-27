package com.prenticeweb.weightlossbuddy.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;

import java.math.BigDecimal;
import java.util.List;

public class BMIActivity extends AppCompatActivity {


    private WeightViewModel viewModel;
    private LiveData<List<WeightMeasurement>> weights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bmi_screen);

        WeightViewModel viewModel = new ViewModelProvider(this).get(WeightViewModel.class);
        this.viewModel = viewModel;
        initData();
    }


    private void initData() {
        weights = viewModel.getReadAll();
        weights.observe(this, weightMeasurements -> {
            initBMIGauge();
        });
    }

    private void initBMIGauge() {
        Range underWeightRange = new Range();
        underWeightRange.setColor(R.color.underweight);
        underWeightRange.setFrom(0.0);
        underWeightRange.setTo(18.5);

        Range normalRange = new Range();
        normalRange.setColor(R.color.normal);
        normalRange.setFrom(18.5);
        normalRange.setTo(24.9);

        Range overweightRange = new Range();
        overweightRange.setColor(R.color.overweight);
        normalRange.setFrom(24.9);
        normalRange.setTo(29.9);

        Range obeseRange = new Range();
        obeseRange.setColor(R.color.obese);
        obeseRange.setFrom(29.9);
        obeseRange.setTo(34.9);

        Range severelyObese = new Range();
        severelyObese.setColor(R.color.severelyObese);
        severelyObese.setFrom(34.9);
        severelyObese.setTo(39.9);

        Range morbidlyObese = new Range();
        morbidlyObese.setColor(R.color.morbidlyObese);
        morbidlyObese.setFrom(39.9);
        morbidlyObese.setTo(45);

        HalfGauge halfGauge = findViewById(R.id.halfGauge);
        halfGauge.addRange(underWeightRange);
        halfGauge.addRange(normalRange);
        halfGauge.addRange(overweightRange);
        halfGauge.addRange(obeseRange);
        halfGauge.addRange(severelyObese);
        halfGauge.addRange(morbidlyObese);


        //set min max and current value
        halfGauge.setMaxValueTextColor(R.color.black);
        halfGauge.setMinValueTextColor(R.color.black);
        BigDecimal currentWeightKg = weights.getValue().get(weights.getValue().size()-1).getWeightKg();
        halfGauge.setMinValue(0);
        halfGauge.setMaxValue(45); //TODO set to 45 if BMi is less than 45 else BMI plus 5
        halfGauge.setValue(26.9);
    }
}
