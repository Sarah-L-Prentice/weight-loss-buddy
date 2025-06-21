package com.prenticeweb.weightlossbuddy.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;
import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;


public class Tab1 extends Fragment {
    private static final String TAG = "Fragment1";
    private WeightViewModel viewModel;
    LiveData<List<WeightMeasurement>> weights;

    private Function<WeightMeasurement, Unit> getUnit = (WeightMeasurement wm) -> new Kilogram(wm.getWeightKg());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeightViewModel viewModel = new ViewModelProvider(this).get(WeightViewModel.class);
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initData(view);
        initTableView(view);
        initAddNewButton(view);
    }

    private void initData(View view) {
        weights = viewModel.getReadAll();
        weights.observe(getViewLifecycleOwner(), weightMeasurements -> {
                initTableView(view);
        });
    }

    private void initAddNewButton(View view) {
        FloatingActionButton newWeightBtn = view.findViewById(R.id.fabAddNew);
        newWeightBtn.setOnClickListener(v -> {
            NewWeightDialogFragment df = new NewWeightDialogFragment(viewModel);
            df.show(getActivity().getSupportFragmentManager(), TAG);
        });
    }

    private void initTableView(View view) {
        if (null == weights.getValue()) {
            return;
        }

        Context context = getActivity();
        TableLayout tableLayout = view.findViewById(R.id.tableLayout1);

        for (int i = 0; i < weights.getValue().size(); i++) {
            TableRow tableRow = new TableRow(context);
            WeightMeasurement wm = weights.getValue().get(i);

            TextView textViewDate = new TextView(context);
            setStyling(textViewDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
            textViewDate.setText(sdf.format(wm.getDate()));
            tableRow.addView(textViewDate);

            TextView textViewWeight = new TextView(context);
            setStyling(textViewWeight);
            textViewWeight.setText(getUnit.apply(wm).getFormattedUnit());
            tableRow.addView(textViewWeight);

            TextView textViewGainLossSince = new TextView(context);
            setStyling(textViewGainLossSince);
            String lossOrGainSince = i == 0 ? "N/A" : getUnit.apply(wm).getQuantity().subtract(getUnit.apply(weights.getValue().get(i - 1)).getQuantity()).toString();
            textViewGainLossSince.setText(lossOrGainSince);
            tableRow.addView(textViewGainLossSince);

            TextView textViewTotal = new TextView(context);
            setStyling(textViewTotal);
            String total = i == 0 ? "N/A" : getUnit.apply(wm).getQuantity().subtract(getUnit.apply(weights.getValue().get(0)).getQuantity()).toString();
            textViewTotal.setText(total);
            tableRow.addView(textViewTotal);

            tableLayout.addView(tableRow);
        }
    }

    private void setStyling(TextView textView) {
        textView.setPadding(10, 10, 10, 10);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
        textView.setBackgroundResource(R.color.lightYellow);
    }

}