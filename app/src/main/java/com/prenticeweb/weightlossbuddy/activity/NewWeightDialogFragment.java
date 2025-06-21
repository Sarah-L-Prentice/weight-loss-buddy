package com.prenticeweb.weightlossbuddy.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.prenticeweb.weightlossbuddy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewWeightDialogFragment extends AppCompatDialogFragment {

    private DatePickerDialog datePicker;
    private Button dateButton;
    private SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_enter_weight);
        initDatePicker();
        dateButton = dialog.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        dateButton.setText(sdf.format(getTodayDate()));

        Button saveButton = dialog.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save to DB

                // close dialog
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date = cal.getTime();
                dateButton.setText(sdf.format(date));
            }
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

    public void openDatePicker(View view) {
        datePicker.show();
    }
}
