package com.prenticeweb.weightlossbuddy.activity;

import static com.prenticeweb.weightlossbuddy.common.Constants.SIMPLE_DATE_FORMAT;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.LiveData;

import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.calculations.HeightConverter;
import com.prenticeweb.weightlossbuddy.calculations.WeightConverter;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo.PreferredWeightUnit;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.view.KeyInfoViewModel;
import com.prenticeweb.weightlossbuddy.room.view.WeightViewModel;
import com.prenticeweb.weightlossbuddy.unit.height.Centimetre;
import com.prenticeweb.weightlossbuddy.unit.height.FeetAndInches;
import com.prenticeweb.weightlossbuddy.unit.height.Inch;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeightEntryDialogFragment extends AppCompatDialogFragment {

    public enum DialogMode {
        WEIGHT_ENTRY,       // New weight entry - date + weight
        TARGET_SETUP        // On Startup - weight + height + preferred unit
    }

    private final DialogMode mode;
    private final WeightViewModel weightViewModel;
    private final KeyInfoViewModel keyInfoViewModel;

    private final LiveData<List<WeightMeasurement>> existingMeasurements;

    public WeightEntryDialogFragment(DialogMode mode, WeightViewModel weightViewModel, KeyInfoViewModel keyInfoViewModel, LiveData<List<WeightMeasurement>> existingMeasurements) {
        this.mode = mode;
        this.weightViewModel = weightViewModel;
        this.keyInfoViewModel = keyInfoViewModel;
        this.existingMeasurements = existingMeasurements;
    }

    private DatePickerDialog datePicker;
    private Button dateButton;

    private EditText kgEditText;
    private TextWatcher kgEditTextWatcher;
    private EditText lbEditText;
    private TextWatcher lbEditTextWatcher;
    private EditText stoneEditText;
    private EditText lbsEditText;
    private TextWatcher stoneAndPoundsEditTextWatcher;

    private EditText heightCmEditText;
    private TextWatcher heightCmEditTextWatcher;
    private EditText heightInchesEditText;
    private TextWatcher heightFeetAndInchesEditTextWatcher;
    private EditText heightFeetEditText;

    private RadioGroup weightUnitRadioGroup;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        
        // Choose layout based on mode
        int layoutId = mode == DialogMode.WEIGHT_ENTRY 
            ? R.layout.custom_dialog_enter_weight 
            : R.layout.custom_dialog_enter_target_weight_and_height;
        dialog.setContentView(layoutId);

        // Initialize weight input fields (common to both modes)
        initKgText(dialog);
        initLbText(dialog);
        initStoneAndPoundsText(dialog);

        // Mode-specific initialization
        if (mode == DialogMode.WEIGHT_ENTRY) {
            initDatePicker();
            initDateButton(dialog);
        } else {
            // TARGET_SETUP mode
            initHeightCmText(dialog);
            initHeightFeetAndInchesText(dialog);
            weightUnitRadioGroup = dialog.findViewById(R.id.radioGroup);
            populateTargetSetupFields(dialog);
        }

        Button saveButton = dialog.findViewById(R.id.save);
        saveButton.setOnClickListener(v -> {
            if (mode == DialogMode.WEIGHT_ENTRY) {
                saveWeightEntryWithCheck(dialog);
            } else {
                saveTargetSetup(dialog);
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void saveWeightEntryWithCheck(Dialog dialog) {
        String dateString = dateButton.getText().toString();
        
        // Check if weight measurement already exists for this date using the existing measurements list
        try {
            Date selectedDate = SIMPLE_DATE_FORMAT.parse(dateString);
            WeightMeasurement existingMeasurement = null;
            
            if (existingMeasurements != null) {
                for (WeightMeasurement measurement : existingMeasurements.getValue()) {
                    if (measurement.getDate().equals(selectedDate)) {
                        existingMeasurement = measurement;
                        break;
                    }
                }
            }
            
            if (existingMeasurement != null) {
                // Show alert dialog for duplicate date
                showDuplicateDateAlert(dateString, dialog);
            } else {
                // No duplicate, proceed with saving
                saveWeightEntry(dialog);
                dialog.dismiss();
            }
        } catch (Exception e) {
            // If there's an error, proceed with saving
            saveWeightEntry(dialog);
            dialog.dismiss();
        }
    }

    private void saveWeightEntry(Dialog dialog) {
        weightViewModel.insert(
            dateButton.getText().toString(),
            kgEditText.getText().toString(),
            lbEditText.getText().toString()
        );
    }

    private void saveTargetSetup(Dialog dialog) {
        BigDecimal heightCm = new BigDecimal(heightCmEditText.getText().toString());
        FeetAndInches feetAndInches = new FeetAndInches(
            new BigDecimal(heightFeetEditText.getText().toString()),
            new BigDecimal(heightInchesEditText.getText().toString())
        );
        Inch heightInches = HeightConverter.convertFeetAndInchesToInch(feetAndInches);
        BigDecimal targetWeightKg = new BigDecimal(kgEditText.getText().toString());
        BigDecimal targetWeightLb = new BigDecimal(lbEditText.getText().toString());
        PreferredWeightUnit preferredWeightUnit = getPreferredWeightUnit();

        keyInfoViewModel.insert(heightCm, heightInches.getQuantity(), targetWeightLb, targetWeightKg, preferredWeightUnit);
    }

    private void populateTargetSetupFields(Dialog dialog) {
        // Observe KeyInfo data and populate fields when available
        keyInfoViewModel.getReadAll().observe(this, keyInfo -> {
            if (keyInfo != null) {
                // Populate weight fields
                setTextSilently(kgEditText, kgEditTextWatcher, keyInfo.getTargetWeightKg().toString());
                setTextSilently(lbEditText, lbEditTextWatcher, keyInfo.getTargetWeightLb().toString());
                
                // Populate stone and pounds fields from target weight
                Kilogram targetKg = new Kilogram(keyInfo.getTargetWeightKg());
                StoneAndPounds stoneAndPounds = WeightConverter.convertKgToStoneAndPounds(targetKg);
                setTextSilentlyForSharedWatcher(stoneEditText, lbsEditText, stoneAndPoundsEditTextWatcher,
                    stoneAndPounds.getQuantity().toString(), stoneAndPounds.getMinorUnit().getScaledUnit());
                
                // Populate height fields
                setTextSilently(heightCmEditText, heightCmEditTextWatcher, keyInfo.getHeightInCm().toString());
                
                // Convert height to feet and inches
                Centimetre cm = new Centimetre(keyInfo.getHeightInCm());
                FeetAndInches feetAndInches = HeightConverter.convertCmToFeetAndInches(cm);
                setTextSilentlyForSharedWatcher(heightFeetEditText, heightInchesEditText, heightFeetAndInchesEditTextWatcher,
                    feetAndInches.getQuantity().toString(), feetAndInches.getMinorUnit().getScaledUnit());
                
                // Set preferred weight unit radio button
                setPreferredWeightUnitRadio(keyInfo.getPreferredWeightUnit());
            }
        });
    }

    private void setPreferredWeightUnitRadio(PreferredWeightUnit preferredWeightUnit) {
        if (weightUnitRadioGroup != null) {
            int radioButtonId = -1;
            switch (preferredWeightUnit) {
                case KG:
                    radioButtonId = R.id.radioKg;
                    break;
                case LB:
                    radioButtonId = R.id.radioLb;
                    break;
                case STONE_AND_POUNDS:
                    radioButtonId = R.id.radioStoneAndPounds;
                    break;
            }
            if (radioButtonId != -1) {
                weightUnitRadioGroup.check(radioButtonId);
            }
        }
    }

    private void initStoneAndPoundsText(Dialog dialog) {
        stoneEditText = dialog.findViewById(R.id.stoneNumber);
        lbsEditText = dialog.findViewById(R.id.lbsNumberDecimal);
        stoneAndPoundsEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    BigDecimal stone = stoneEditText.getText().toString().isEmpty() 
                        ? BigDecimal.ZERO 
                        : new BigDecimal(stoneEditText.getText().toString());
                    BigDecimal lbs = lbsEditText.getText().toString().isEmpty() 
                        ? BigDecimal.ZERO 
                        : new BigDecimal(lbsEditText.getText().toString());
                    StoneAndPounds stoneAndPounds = new StoneAndPounds(stone, lbs, false);
                    Pound pound = WeightConverter.convertStoneAndPoundsToPounds(stoneAndPounds);
                    Kilogram kg = WeightConverter.convertLbToKg(pound);
                    setTextSilently(lbEditText, lbEditTextWatcher, pound.getScaledUnit());
                    setTextSilently(kgEditText, kgEditTextWatcher, kg.getScaledUnit());
                }
            }
        };
        stoneEditText.addTextChangedListener(stoneAndPoundsEditTextWatcher);
        lbsEditText.addTextChangedListener(stoneAndPoundsEditTextWatcher);
    }

    private void initKgText(Dialog dialog) {
        kgEditText = dialog.findViewById(R.id.kgNumberDecimal);
        kgEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    Kilogram kg = new Kilogram(new BigDecimal(s.toString()));
                    Pound pound = WeightConverter.convertKgToLb(kg);
                    StoneAndPounds stoneAndPounds = WeightConverter.convertKgToStoneAndPounds(kg);
                    setTextSilently(lbEditText, lbEditTextWatcher, pound.getScaledUnit());
                    setTextSilentlyForSharedWatcher(stoneEditText, lbsEditText, stoneAndPoundsEditTextWatcher, 
                        stoneAndPounds.getQuantity().toString(), stoneAndPounds.getMinorUnit().getScaledUnit());
                }
            }
        };
        kgEditText.addTextChangedListener(kgEditTextWatcher);
    }

    private void initLbText(Dialog dialog) {
        lbEditText = dialog.findViewById(R.id.lbNumberDecimal);
        lbEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    Pound pound = new Pound(new BigDecimal(s.toString()));
                    Kilogram kg = WeightConverter.convertLbToKg(pound);
                    StoneAndPounds stoneAndPounds = WeightConverter.convertKgToStoneAndPounds(kg);
                    setTextSilently(kgEditText, kgEditTextWatcher, kg.getScaledUnit());
                    setTextSilentlyForSharedWatcher(stoneEditText, lbsEditText, stoneAndPoundsEditTextWatcher, 
                        stoneAndPounds.getQuantity().toString(), stoneAndPounds.getMinorUnit().getScaledUnit());
                }
            }
        };
        lbEditText.addTextChangedListener(lbEditTextWatcher);
    }

    private void initDateButton(Dialog dialog) {
        dateButton = dialog.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(view -> openDatePicker());
        dateButton.setText(SIMPLE_DATE_FORMAT.format(getTodayDate()));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date = cal.getTime();
            dateButton.setText(SIMPLE_DATE_FORMAT.format(date));
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(this.getActivity(), 0, dateSetListener, year, month, day);
    }

    private void showDuplicateDateAlert(String dateString, Dialog parentDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Duplicate Entry");
        builder.setMessage("Weight measurement for the date " + dateString + " already exists. Do you want to overwrite it?");
        
        builder.setPositiveButton("Overwrite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Proceed with overwrite
                saveWeightEntry(parentDialog);
                parentDialog.dismiss();
            }
        });
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Don't save, keep dialog open
                dialog.dismiss();
            }
        });
        
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Date getTodayDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public void openDatePicker() {
        datePicker.show();
    }

    private void initHeightCmText(Dialog dialog) {
        heightCmEditText = dialog.findViewById(R.id.cmNumberDecimal);
        heightCmEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    Centimetre cm = new Centimetre(new BigDecimal(s.toString()));
                    FeetAndInches feetAndInches = HeightConverter.convertCmToFeetAndInches(cm);
                    setTextSilentlyForSharedWatcher(heightFeetEditText, heightInchesEditText, heightFeetAndInchesEditTextWatcher, 
                        feetAndInches.getQuantity().toString(), feetAndInches.getMinorUnit().getScaledUnit());
                }
            }
        };
        heightCmEditText.addTextChangedListener(heightCmEditTextWatcher);
    }

    private void initHeightFeetAndInchesText(Dialog dialog) {
        heightFeetEditText = dialog.findViewById(R.id.feetNumber);
        heightInchesEditText = dialog.findViewById(R.id.inchesNumberDecimal);
        heightFeetAndInchesEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    BigDecimal feet = heightFeetEditText.getText().toString().isEmpty() 
                        ? BigDecimal.ZERO 
                        : new BigDecimal(heightFeetEditText.getText().toString());
                    BigDecimal inches = heightInchesEditText.getText().toString().isEmpty() 
                        ? BigDecimal.ZERO 
                        : new BigDecimal(heightInchesEditText.getText().toString());
                    FeetAndInches feetAndInches = new FeetAndInches(feet, inches);
                    Centimetre cm = HeightConverter.convertFeetAndInchesToCm(feetAndInches);
                    setTextSilently(heightCmEditText, heightCmEditTextWatcher, cm.getScaledUnit());
                }
            }
        };
        heightInchesEditText.addTextChangedListener(heightFeetAndInchesEditTextWatcher);
        heightFeetEditText.addTextChangedListener(heightFeetAndInchesEditTextWatcher);
    }

    private PreferredWeightUnit getPreferredWeightUnit() {
        int selectedRadioButtonId = weightUnitRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            return PreferredWeightUnit.KG;
        }
        
        RadioButton selectedRadioButton = weightUnitRadioGroup.findViewById(selectedRadioButtonId);
        String selectedText = selectedRadioButton.getText().toString();
        
        if (selectedText.equals(getString(R.string.kg))) {
            return PreferredWeightUnit.KG;
        } else if (selectedText.equals(getString(R.string.lb))) {
            return PreferredWeightUnit.LB;
        } else if (selectedText.equals(getString(R.string.stone_and_pounds))) {
            return PreferredWeightUnit.STONE_AND_POUNDS;
        } else {
            return PreferredWeightUnit.KG;
        }
    }

    private static void setTextSilently(EditText editText, TextWatcher textWatcher, CharSequence text) {
        editText.removeTextChangedListener(textWatcher);
        editText.setText(text);
        editText.addTextChangedListener(textWatcher);
    }

    private static void setTextSilentlyForSharedWatcher(EditText editText1, EditText editText2, TextWatcher textWatcher, CharSequence text1, CharSequence text2) {
        editText1.removeTextChangedListener(textWatcher);
        editText2.removeTextChangedListener(textWatcher);
        editText1.setText(text1);
        editText2.setText(text2);
        editText1.addTextChangedListener(textWatcher);
        editText2.addTextChangedListener(textWatcher);
    }
}
