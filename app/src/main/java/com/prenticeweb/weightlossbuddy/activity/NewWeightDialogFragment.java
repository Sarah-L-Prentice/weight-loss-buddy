package com.prenticeweb.weightlossbuddy.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.calculations.WeightConverter;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewWeightDialogFragment extends AppCompatDialogFragment {

    private final WeightViewModel viewModel;

    public NewWeightDialogFragment(WeightViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private DatePickerDialog datePicker;
    private Button dateButton;

    private EditText kgEditText;
    private TextWatcher kgEditTextWatcher;
    private EditText lbEditText;
    private TextWatcher lbEditTextWatcher;
    private EditText stoneEditText;
    private EditText lbsEditText;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_enter_weight);
        initDatePicker();
        initDateButton(dialog);
        initKgText(dialog);
        initLbText(dialog);

        stoneEditText = dialog.findViewById(R.id.stoneNumber);
        lbsEditText = dialog.findViewById(R.id.lbsNumberDecimal);

        Button saveButton = dialog.findViewById(R.id.save);
        saveButton.setOnClickListener(v -> {
            // Save to DB
            viewModel.insert(dateButton.getText().toString(), kgEditText.getText().toString(), lbEditText.getText().toString());
            // close dialog
            dialog.dismiss();
        });
        return dialog;
    }

    private void initKgText(Dialog dialog) {
        kgEditText = dialog.findViewById(R.id.kgNumberDecimal);
        kgEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Kilogram kg = new Kilogram(new BigDecimal(s.toString()));
                Pound pound = WeightConverter.convertKgToLb(kg);
                StoneAndPounds stoneAndPounds = WeightConverter.convertKgToStoneAndPounds(kg);
                setTextSilently(lbEditText, lbEditTextWatcher, pound.getScaledUnit());
                stoneEditText.setText(stoneAndPounds.getQuantity().toString());
                lbsEditText.setText(stoneAndPounds.getMinorUnit().getScaledUnit());
            }
        };
        kgEditText.addTextChangedListener(kgEditTextWatcher);
    }

    private void initLbText(Dialog dialog) {
        lbEditText = dialog.findViewById(R.id.lbNumberDecimal);
        lbEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pound pound = new Pound(new BigDecimal(s.toString()));
                Kilogram kg = WeightConverter.convertLbToKg(pound);
                StoneAndPounds stoneAndPounds = WeightConverter.convertPoundsToStoneAndPounds(pound);

                setTextSilently(kgEditText, kgEditTextWatcher, kg.getScaledUnit());
                stoneEditText.setText(stoneAndPounds.getQuantity().toString());
                lbsEditText.setText(stoneAndPounds.getMinorUnit().getScaledUnit());
            }
        };
        lbEditText.addTextChangedListener(lbEditTextWatcher);
    }

    private void initDateButton(Dialog dialog) {
        dateButton = dialog.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(view -> openDatePicker());
        dateButton.setText(sdf.format(getTodayDate()));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date = cal.getTime();
            dateButton.setText(sdf.format(date));
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(this.getActivity(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
    }

    private Date getTodayDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public void openDatePicker() {
        datePicker.show();
    }

    private static void setTextSilently(EditText editText, TextWatcher textWatcher, CharSequence text) {
        editText.removeTextChangedListener(textWatcher); //remove watcher temporarily
        editText.setText(text); //set text
        editText.addTextChangedListener(textWatcher); //re-adding watcher
    }
}
