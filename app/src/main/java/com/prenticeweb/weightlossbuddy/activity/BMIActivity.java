package com.prenticeweb.weightlossbuddy.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.calculations.BMICalculator;
import com.prenticeweb.weightlossbuddy.gauge.BMIGaugeKt;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;
import com.prenticeweb.weightlossbuddy.unit.height.Metre;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;

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
        ComposeView view = findViewById(R.id.halfGaugeBmi);
        WeightMeasurement currentWeight = weights.getValue().get(weights.getValue().size()-1);
        Kilogram currentWeightKg = new Kilogram(currentWeight.getWeightKg());
        BigDecimal bmi = BMICalculator.calculateBMI(currentWeightKg, new Metre(BigDecimal.valueOf(1.73)));
        BMIGaugeKt.setContent(view, bmi.floatValue());
    }
}
