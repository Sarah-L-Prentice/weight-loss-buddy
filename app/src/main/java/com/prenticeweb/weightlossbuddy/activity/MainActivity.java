package com.prenticeweb.weightlossbuddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.calculations.BMICalculator;
import com.prenticeweb.weightlossbuddy.calculations.HeightConverter;
import com.prenticeweb.weightlossbuddy.calculations.WeightConverter;
import com.prenticeweb.weightlossbuddy.gauge.GaugeKt;
import com.prenticeweb.weightlossbuddy.gauge.ProgressGauge;
import com.prenticeweb.weightlossbuddy.gauge.ProgressGaugeKt;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.KeyInfoViewModel;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;
import com.prenticeweb.weightlossbuddy.unit.height.Centimetre;
import com.prenticeweb.weightlossbuddy.unit.height.Metre;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private LineChart chart;
    private WeightViewModel weightViewModel;
    private KeyInfoViewModel keyInfoViewModel;
    private LiveData<List<WeightMeasurement>> weights;
    private LiveData<KeyInfo> keyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        this.keyInfoViewModel = new ViewModelProvider(this).get(KeyInfoViewModel.class);
        initKeyInfoData();

        this.weightViewModel = new ViewModelProvider(this).get(WeightViewModel.class);

        initWeightMeasurementsData();
        initCurrentWeightTile();
        initTargetWeightTile();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cardViewBMI) {
            Intent intent = new Intent(this, BMIActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cardViewCurrentWeight) {
            Intent intent = new Intent(this, ScreenSliderPagerActivity.class);
            startActivity(intent);
        }
    }

    private void initTargetWeightTile() {
        TextView textTargetWeightAmount = findViewById(R.id.textTargetWeightAmount);
        keyInfo.observe(this, keyInfoData -> {
            WeightMeasurement wm = new WeightMeasurement();
            wm.setWeightKg(keyInfoData.getTargetWeightKg());
            wm.setWeightLb(keyInfoData.getTargetWeightLb());
            textTargetWeightAmount.setText(wm.getFormattedWeight(keyInfoData.getPreferredWeightUnit(), false));
        });
    }

    private void initCurrentWeightTile() {
        TextView textCurrentWeightAmount = findViewById(R.id.textCurrentWeightAmount);
        
        // Observe both weights and keyInfo to ensure both are available
        weights.observe(this, weightMeasurements -> {
            updateCurrentWeightDisplay(weightMeasurements, textCurrentWeightAmount);
        });
        
        keyInfo.observe(this, keyInfoData -> {
            updateCurrentWeightDisplay(weights.getValue(), textCurrentWeightAmount);
        });
    }
    
    private void updateCurrentWeightDisplay(List<WeightMeasurement> weightMeasurements, TextView textCurrentWeightAmount) {

        ComposeView bmiView = findViewById(R.id.halfGaugeBmi);

        List<WeightMeasurement> weights = weightMeasurements;
        KeyInfo keyInfoData = keyInfo.getValue();
        
        if (weights != null && !weights.isEmpty() && keyInfoData != null) {
            // Find the most recent weight measurement
            WeightMeasurement mostRecent = weights.stream()
                    .max(Comparator.comparing(WeightMeasurement::getDate))
                    .orElse(null);

            WeightMeasurement startWeight = weights.stream()
                    .min(Comparator.comparing(WeightMeasurement::getDate))
                    .orElse(null);
            
            if (mostRecent != null) {
                String formattedCurrentWeight = mostRecent.getFormattedWeight(keyInfoData.getPreferredWeightUnit(), false);
                textCurrentWeightAmount.setText(formattedCurrentWeight);
                Metre heightMetres = HeightConverter.convertCentimetreToMetres(new Centimetre(keyInfoData.getHeightInCm()));
                Kilogram currentWeightKg = new Kilogram(mostRecent.getWeightKg());
                BigDecimal bmi = BMICalculator.calculateBMI(currentWeightKg, heightMetres);
                GaugeKt.setContent(bmiView, bmi.floatValue());


                ComposeView view = findViewById(R.id.progressGauge);
                WeightMeasurement targetWeight = new WeightMeasurement();
                targetWeight.setWeightKg(keyInfoData.getTargetWeightKg());
                targetWeight.setWeightLb(keyInfoData.getTargetWeightLb());
                String formattedTargetWeight = targetWeight.getFormattedWeight(keyInfoData.getPreferredWeightUnit(), false);
                WeightMeasurement weightLost = mostRecent.subtract(startWeight);
                String formattedWeightLost = weightLost.getFormattedWeight(keyInfoData.getPreferredWeightUnit(), true);

                // Calculate total weight to lose (start weight - target weight)
                WeightMeasurement totalWeightToLose = startWeight.subtract(targetWeight);
                
                // Calculate percentage of weight lost as decimal
                float percentageLost = weightLost.getWeightKg()
                            .divide(totalWeightToLose.getWeightKg(), 2, RoundingMode.HALF_UP)
                            .floatValue();

                ProgressGaugeKt.setContent(view, percentageLost, formattedWeightLost, formattedTargetWeight);
            }
        }
    }

    private void initWeightMeasurementsData() {
        weights = weightViewModel.getReadAll();
        weights.observe(this, weightMeasurements -> {
            createLineChart();
        });
    }

    private void initKeyInfoData() {
        keyInfo = keyInfoViewModel.getReadAll();
        
        // Observe the LiveData and show dialog only when data is loaded
        keyInfo.observe(this, keyInfoData -> {
            if (keyInfoData == null) {
                OnStartupDialogFragment dialog = new OnStartupDialogFragment(keyInfoViewModel);
                dialog.show(getSupportFragmentManager(), "OnStartupDialogFragment");
            }
        });
    }

    private void createLineChart() {
        chart = findViewById(R.id.lineChart);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < weights.getValue().size(); i++) {
            WeightMeasurement wm = weights.getValue().get(i);
            yValues.add(new Entry(i, wm.getWeightKg().setScale(1, RoundingMode.HALF_UP).floatValue()));
        }

        LineDataSet set1 = new LineDataSet(yValues, getString((R.string.weight)));
        set1.setFillAlpha(110);
        set1.setLineWidth(3);
        set1.setValueTextSize(10f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        List<String> xValues = weights.getValue()
                .stream()
                .map(wm -> df.format(wm.getDate())).collect(Collectors.toList());
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}