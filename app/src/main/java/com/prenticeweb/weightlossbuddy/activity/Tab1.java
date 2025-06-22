package com.prenticeweb.weightlossbuddy.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
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
        return inflater.inflate(R.layout.fragment_weights_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initData(view);
        initGrid(view);
        initAddNewButton(view);
    }

    private void initData(View view) {
        weights = viewModel.getReadAll();
        weights.observe(getViewLifecycleOwner(), weightMeasurements -> {
            initGrid(view);
        });
    }

    private void initAddNewButton(View view) {
        FloatingActionButton newWeightBtn = view.findViewById(R.id.fabAddNew);
        newWeightBtn.setOnClickListener(v -> {
            NewWeightDialogFragment df = new NewWeightDialogFragment(viewModel);
            df.show(getActivity().getSupportFragmentManager(), TAG);
        });
    }

    private void initGrid(View view) {
        if (null == weights.getValue()) {
            return;
        }

        Context context = getActivity();
        GridLayout gridLayout = view.findViewById(R.id.mainGrid);
        if (gridLayout.getChildCount() > 5) {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View vw = gridLayout.getChildAt(i);
                int id = vw.getId();
                if(id == -1) {
                    gridLayout.removeView(vw);
                }
            }
            gridLayout.invalidate();
        }

        for (int i = 0; i < weights.getValue().size(); i++) {
            WeightMeasurement wm = weights.getValue().get(i);
            int rowRef = i + 1;

            TextView textViewDate = new TextView(context);
            setStyling(textViewDate);
            var params1 = getLayoutParams(0, rowRef);
            textViewDate.setLayoutParams(params1);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
            textViewDate.setText(sdf.format(wm.getDate()));
            gridLayout.addView(textViewDate, rowRef);

            TextView textViewWeight = new TextView(context);
            setStyling(textViewWeight);
            var params2 = getLayoutParams(1, rowRef);
            textViewWeight.setLayoutParams(params2);
            textViewWeight.setText(getUnit.apply(wm).getFormattedUnit());
            gridLayout.addView(textViewWeight, rowRef);

            TextView textViewGainLossSince = new TextView(context);
            setStyling(textViewGainLossSince);
            var params3 = getLayoutParams(2, rowRef);
            textViewGainLossSince.setLayoutParams(params3);
            String lossOrGainSince = i == 0 ? "N/A" : getUnit.apply(wm).getQuantity().subtract(getUnit.apply(weights.getValue().get(i - 1)).getQuantity()).toString();
            textViewGainLossSince.setText(lossOrGainSince);
            gridLayout.addView(textViewGainLossSince, rowRef);

            TextView textViewTotal = new TextView(context);
            setStyling(textViewTotal);
            var params4 = getLayoutParams(3, rowRef);
            textViewTotal.setLayoutParams(params4);
            String total = i == 0 ? "N/A" : getUnit.apply(wm).getQuantity().subtract(getUnit.apply(weights.getValue().get(0)).getQuantity()).toString();
            textViewTotal.setText(total);
            gridLayout.addView(textViewTotal, rowRef);

            ImageButton imageButton = new ImageButton(context);
            imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            imageButton.setImageResource(android.R.drawable.ic_menu_delete);
            var params5 = getLayoutParams(4, rowRef);
            imageButton.setLayoutParams(params5);
            gridLayout.addView(imageButton, rowRef);
            imageButton.setOnClickListener(getDeleteListener(textViewDate));
        }
    }

    private View.OnClickListener getDeleteListener(TextView dateTextView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.delete(dateTextView.getText().toString());
            }
        };
    }

    private GridLayout.LayoutParams getLayoutParams(int col, int row) {
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(col);
        param.rowSpec = GridLayout.spec(row);
        return param;
    }

    private void setStyling(TextView textView) {
        textView.setPadding(10, 10, 10, 10);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
    }

}