package com.prenticeweb.weightlossbuddy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.val;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private LineChart chart;
    private WeightViewModel viewModel;
    private LiveData<List<WeightMeasurement>> weights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        WeightViewModel viewModel = new ViewModelProvider(this).get(WeightViewModel.class);
        this.viewModel = viewModel;
        initData();
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

    private void initData() {
        weights = viewModel.getReadAll();
        weights.observe(this, weightMeasurements -> {
            createLineChart();
        });
    }

    private void createLineChart() {
        chart = findViewById(R.id.lineChart);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);

        LimitLine targetWeight = new LimitLine(35f);
        targetWeight.setLineWidth(4f);
        targetWeight.enableDashedLine(10f, 10f, 0f);
        targetWeight.setTextSize(15f);
        targetWeight.setLabel(getString(R.string.target_weight));

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