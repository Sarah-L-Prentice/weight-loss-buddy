package com.prenticeweb.weightlossbuddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.KeyInfoViewModel;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        KeyInfoViewModel keyInfoViewModel = new ViewModelProvider(this).get(KeyInfoViewModel.class);
        this.keyInfoViewModel = keyInfoViewModel;
        initKeyInfoData();

        WeightViewModel weightViewModel = new ViewModelProvider(this).get(WeightViewModel.class);
        this.weightViewModel = weightViewModel;

        initWeightMeasurementsData();
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