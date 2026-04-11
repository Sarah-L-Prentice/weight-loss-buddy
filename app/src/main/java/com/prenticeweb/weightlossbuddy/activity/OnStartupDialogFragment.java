package com.prenticeweb.weightlossbuddy.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.calculations.HeightConverter;
import com.prenticeweb.weightlossbuddy.calculations.WeightConverter;
import com.prenticeweb.weightlossbuddy.room.view.KeyInfoViewModel;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo.PreferredWeightUnit;
import com.prenticeweb.weightlossbuddy.unit.height.Centimetre;
import com.prenticeweb.weightlossbuddy.unit.height.FeetAndInches;
import com.prenticeweb.weightlossbuddy.unit.height.Inch;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

import java.math.BigDecimal;

public class OnStartupDialogFragment extends AppCompatDialogFragment {

    private final KeyInfoViewModel viewModel;

    public OnStartupDialogFragment(KeyInfoViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private EditText heightCmEditText;
    private TextWatcher heightCmEditTextWatcher;
    private EditText heightInchesEditText;
    private TextWatcher heightFeetAndInchesEditTextWatcher;
    private EditText heightFeetEditText;
    private EditText kgEditText;
    private TextWatcher kgEditTextWatcher;
    private EditText lbEditText;
    private TextWatcher lbEditTextWatcher;
    private EditText stoneEditText;
    private EditText lbsMinorUnitEditText;
    private TextWatcher stoneAndPoundsEditTextWatcher;
    private RadioGroup weightUnitRadioGroup;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_enter_target_weight_and_height);

        // Initialize weight input fields
        initKgText(dialog);
        initLbText(dialog);
        initStoneAndPoundsText(dialog);

        // Initialize height input fields
        initHeightCmText(dialog);
        initHeightFeetAndInchesText(dialog);

        // Initialize radio group
        weightUnitRadioGroup = dialog.findViewById(R.id.radioGroup);

        Button saveButton = dialog.findViewById(R.id.save);
        saveButton.setOnClickListener(v -> {
            // Save to DB
            BigDecimal heightCm = new BigDecimal(heightCmEditText.getText().toString());

            FeetAndInches feetAndInches = new FeetAndInches(new BigDecimal(heightFeetEditText.getText().toString()), new BigDecimal(heightInchesEditText.getText().toString()));
            Inch heightInches = HeightConverter.convertFeetAndInchesToInch(feetAndInches);
            BigDecimal targetWeightKg = new BigDecimal(kgEditText.getText().toString());
            BigDecimal targetWeightLb = new BigDecimal(lbEditText.getText().toString());

            // Get preferred weight unit from radio buttons
            PreferredWeightUnit preferredWeightUnit = getPreferredWeightUnit();

            viewModel.insert(heightCm, heightInches.getQuantity(), targetWeightLb, targetWeightKg, preferredWeightUnit);
            // close dialog
            dialog.dismiss();
        });
        return dialog;
    }

    private void initStoneAndPoundsText(Dialog dialog) {
        stoneEditText = dialog.findViewById(R.id.stoneNumber);
        lbsMinorUnitEditText = dialog.findViewById(R.id.lbsNumberDecimal);
        stoneAndPoundsEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    BigDecimal stone = stoneEditText.getText().toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(stoneEditText.getText().toString());
                    BigDecimal lbs = lbsMinorUnitEditText.getText().toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(lbsMinorUnitEditText.getText().toString());
                    StoneAndPounds stoneAndPounds = new StoneAndPounds(stone, lbs);
                    Pound pound = WeightConverter.convertStoneAndPoundsToPounds(stoneAndPounds);
                    Kilogram kg = WeightConverter.convertLbToKg(pound);
                    setTextSilently(lbEditText, lbEditTextWatcher, pound.getScaledUnit());
                    setTextSilently(kgEditText, kgEditTextWatcher, kg.getScaledUnit());
                }
            }
        };
        stoneEditText.addTextChangedListener(stoneAndPoundsEditTextWatcher);
        lbsMinorUnitEditText.addTextChangedListener(stoneAndPoundsEditTextWatcher);
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
                if (!s.toString().isEmpty()) {
                    Kilogram kg = new Kilogram(new BigDecimal(s.toString()));
                    Pound pound = WeightConverter.convertKgToLb(kg);
                    StoneAndPounds stoneAndPounds = WeightConverter.convertKgToStoneAndPounds(kg);
                    setTextSilently(lbEditText, lbEditTextWatcher, pound.getScaledUnit());
                    setTextSilentlyForSharedWatcher(stoneEditText, lbsMinorUnitEditText, stoneAndPoundsEditTextWatcher, stoneAndPounds.getScaledUnit(), stoneAndPounds.getMinorUnit().getScaledUnit());
                }
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
                if (!s.toString().isEmpty()) {
                    Pound pound = new Pound(new BigDecimal(s.toString()));
                    Kilogram kg = WeightConverter.convertLbToKg(pound);
                    StoneAndPounds stoneAndPounds = WeightConverter.convertKgToStoneAndPounds(kg);
                    setTextSilently(kgEditText, kgEditTextWatcher, kg.getScaledUnit());
                    setTextSilentlyForSharedWatcher(stoneEditText, lbsMinorUnitEditText, stoneAndPoundsEditTextWatcher, stoneAndPounds.getScaledUnit(), stoneAndPounds.getMinorUnit().getScaledUnit());
                }
            }
        };
        lbEditText.addTextChangedListener(lbEditTextWatcher);
    }

    private void initHeightCmText(Dialog dialog) {
        heightCmEditText = dialog.findViewById(R.id.cmNumberDecimal);
        heightCmEditTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    Centimetre cm = new Centimetre(new BigDecimal(s.toString()));
                    FeetAndInches feetAndInches = HeightConverter.convertCmToFeetAndInches(cm);
                    setTextSilentlyForSharedWatcher(heightFeetEditText, heightInchesEditText, heightFeetAndInchesEditTextWatcher, feetAndInches.getScaledUnit(), feetAndInches.getMinorUnit().getScaledUnit());
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    BigDecimal feet = heightFeetEditText.getText().toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(heightFeetEditText.getText().toString());
                    BigDecimal inches = heightInchesEditText.getText().toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(heightInchesEditText.getText().toString());
                    FeetAndInches feetAndInches = new FeetAndInches(feet, inches);
                    Centimetre cm = HeightConverter.convertFeetAndInchesToCm(feetAndInches);
                    setTextSilently(heightCmEditText, heightCmEditTextWatcher, cm.getScaledUnit());
                }
            }
        };
        heightInchesEditText.addTextChangedListener(heightFeetAndInchesEditTextWatcher);
        heightFeetEditText.addTextChangedListener(heightFeetAndInchesEditTextWatcher);
    }

    private static void setTextSilently(EditText editText, TextWatcher textWatcher, CharSequence text) {
        editText.removeTextChangedListener(textWatcher); //remove watcher temporarily
        editText.setText(text); //set text
        editText.addTextChangedListener(textWatcher); //re-adding watcher
    }

    private static void setTextSilentlyForSharedWatcher(EditText editText1, EditText editText2, TextWatcher textWatcher, CharSequence text1, CharSequence text2) {
        editText1.removeTextChangedListener(textWatcher); //remove watcher temporarily
        editText2.removeTextChangedListener(textWatcher); //remove watcher temporarily
        editText1.setText(text1); //set text
        editText2.setText(text2); //set text
        editText1.addTextChangedListener(textWatcher); //re-adding watcher
        editText2.addTextChangedListener(textWatcher); //re-adding watcher
    }

    private PreferredWeightUnit getPreferredWeightUnit() {
        int selectedRadioButtonId = weightUnitRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            // Default to KG if nothing is selected
            return PreferredWeightUnit.KG;
        }
        
        RadioButton selectedRadioButton = weightUnitRadioGroup.findViewById(selectedRadioButtonId);
        String selectedText = selectedRadioButton.getText().toString();
        
        // Map radio button text to enum values
        if (selectedText.equals(getString(R.string.kg))) {
            return PreferredWeightUnit.KG;
        } else if (selectedText.equals(getString(R.string.lb))) {
            return PreferredWeightUnit.LB;
        } else if (selectedText.equals(getString(R.string.stone_and_pounds))) {
            return PreferredWeightUnit.STONE_AND_POUNDS;
        } else {
            // Default fallback
            return PreferredWeightUnit.KG;
        }
    }
}
