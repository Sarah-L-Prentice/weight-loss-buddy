package com.prenticeweb.weightlossbuddy.activity;

import static com.prenticeweb.weightlossbuddy.common.Constants.SIMPLE_DATE_FORMAT;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class ViewWeightMeasurementsFragment extends Fragment {
    private static final String TAG = "ViewWeightMeasurementsFragment";
    private WeightViewModel viewModel;
    LiveData<List<WeightMeasurement>> weights;
    private final Function<WeightMeasurement, Unit> getUnit;

    public ViewWeightMeasurementsFragment(Function<WeightMeasurement, Unit> getUnit) {
        this.getUnit = getUnit;
    }


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
        List<View> toRemove = new ArrayList<>();
        if (gridLayout.getChildCount() > 5) {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View vw = gridLayout.getChildAt(i);
                int id = vw.getId();
                if(id == -1) {
                    toRemove.add(vw);
                }
            }
            toRemove.stream().forEach(vw -> gridLayout.removeView(vw));
            gridLayout.invalidate();
        }

        for (int i = 0; i < weights.getValue().size(); i++) {
            WeightMeasurement wm = weights.getValue().get(i);
            int rowRef = i + 1;

            TextView textViewDate = new TextView(context);
            setStyling(textViewDate,0, rowRef);
            SimpleDateFormat sdf = SIMPLE_DATE_FORMAT;
            textViewDate.setText(sdf.format(wm.getDate()));
            gridLayout.addView(textViewDate, rowRef);

            TextView textViewWeight = new TextView(context);
            setStyling(textViewWeight,1, rowRef);
            textViewWeight.setText(getUnit.apply(wm).getFormattedUnit());
            gridLayout.addView(textViewWeight, rowRef);

            TextView textViewGainLossSince = new TextView(context);
            setStyling(textViewGainLossSince, 2, rowRef);
            String lossOrGainSince = i == 0 ? "N/A" : getUnit.apply(wm).subtract(getUnit.apply(weights.getValue().get(i - 1))).getSignedFormattedUnit();
            textViewGainLossSince.setText(lossOrGainSince);
            gridLayout.addView(textViewGainLossSince, rowRef);

            TextView textViewTotal = new TextView(context);
            setStyling(textViewTotal, 3, rowRef);
            String total = i == 0 ? "N/A" : getUnit.apply(wm).subtract(getUnit.apply(weights.getValue().get(0))).getSignedFormattedUnit();
            textViewTotal.setText(total);
            gridLayout.addView(textViewTotal, rowRef);

            ImageButton imageButton = new ImageButton(context);
            imageButton.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            imageButton.setImageResource(android.R.drawable.ic_notification_clear_all);
            var params5 = getLayoutParams(4, rowRef);
            imageButton.setLayoutParams(params5);
            imageButton.setPadding(5, 5, 5, 5);
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
        param.setGravity(Gravity.FILL_HORIZONTAL);
        param.columnSpec = GridLayout.spec(col, 1f);
        param.rowSpec = GridLayout.spec(row);
        param.height = 70;
        param.width = 0;
        return param;
    }

    private void setStyling(TextView textView, int col, int row) {
        textView.setPadding(5, 5, 5, 5);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
        GridLayout.LayoutParams params = getLayoutParams(col, row);
        textView.setLayoutParams(params);
    }

}