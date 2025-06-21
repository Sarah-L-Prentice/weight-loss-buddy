package com.prenticeweb.weightlossbuddy.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.unit.Unit;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;

import java.math.BigDecimal;
import java.util.function.Function;


public class Tab1 extends Fragment {
    private static final String TAG = "Fragment1";
//    private WeightViewModel viewModel;
//    LiveData<List<WeightMeasurement>> weights;

    private Function<WeightMeasurement, Unit> getUnit = (WeightMeasurement wm) -> new Kilogram(wm.getWeightKg());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WeightViewModel viewModel = new ViewModelProvider(this).get(WeightViewModel.class);
//        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        weights = viewModel.getReadAll();
//        weights.observe(getViewLifecycleOwner(), new Observer<List<WeightMeasurement>>() {
//            @Override
//            public void onChanged(List<WeightMeasurement> weightMeasurements) {
//                initTableView(view);
//            }
//        });
        initTableView(view);
        FloatingActionButton newWeightBtn = view.findViewById(R.id.fabAddNew);
        newWeightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void initTableView(View view) {

        //random data
        String[] dateArr = {"2025-05-07", "2025-05-14",
                "2025-05-21"};
        String[] weightArr = {"80", "79",
                "78"};

        Context context = getActivity();
        TableLayout tableLayout = view.findViewById(R.id.tableLayout1);

//        for (int i = 0; i < weights.getValue().size(); i++) {
        for (int i = 0; i < weightArr.length; i++) {
            TableRow tableRow = new TableRow(context);
//            WeightMeasurement wm = weights.getValue().get(i);
            TextView textViewDate = new TextView(context);
            setStyling(textViewDate);
//            textViewDate.setText(wm.getDate().toString());
            textViewDate.setText(dateArr[i]);
            tableRow.addView(textViewDate);

            TextView textViewWeight = new TextView(context);
            setStyling(textViewWeight);
//            textViewWeight.setText(getUnit.apply(wm).getFormattedUnit());
            textViewWeight.setText(weightArr[i]);
            tableRow.addView(textViewWeight);

            TextView textViewGainLossSince = new TextView(context);
            setStyling(textViewGainLossSince);
            String lossOrGainSince = i == 0 ? "N/A" : new BigDecimal(weightArr[i]).subtract(new BigDecimal(weightArr[i-1])).toString();
            textViewGainLossSince.setText(lossOrGainSince);
            tableRow.addView(textViewGainLossSince);

            TextView textViewTotal = new TextView(context);
            setStyling(textViewTotal);
            String total = i == 0 ? "N/A" : new BigDecimal(weightArr[i]).subtract(new BigDecimal(weightArr[0])).toString();
            textViewTotal.setText(total);
            tableRow.addView(textViewTotal);

            tableLayout.addView(tableRow);
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_enter_weight);
        dialog.show();
        Button saveButton = dialog.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save to DB

                // close dialog
                dialog.dismiss();
            }
        });
    }

    private void setStyling(TextView textView) {
        textView.setPadding(10, 10,10, 10);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
        textView.setBackgroundResource(R.color.lightYellow);
    }

}