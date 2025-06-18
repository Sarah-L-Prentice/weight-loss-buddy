package com.prenticeweb.weightlossbuddy.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.prenticeweb.weightlossbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText enterDate;
    private EditText enterWeight;
    private Button saveButton;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //random data
        String[] dates = {"2025-05-07", "2025-05-14",
                "2025-05-21"};
        String[] weights = {"80", "79",
                "78"};

        Context context = getActivity();

        TableLayout tableLayout = view.findViewById(R.id.tableLayout1);

        for (int i = 0; i < dates.length; i++) {
            TableRow tableRow = new TableRow(context);

            TextView textViewDate = new TextView(context);
            setStyling(textViewDate);
            textViewDate.setText(dates[i]);
            tableRow.addView(textViewDate);

            TextView textViewWeight = new TextView(context);
            setStyling(textViewWeight);
            textViewWeight.setText(weights[i]);
            tableRow.addView(textViewWeight);

            tableLayout.addView(tableRow);
        }
        TableRow tableRow = new TableRow(context);

        EditText enterDate = new EditText(context);
        setStyling(enterDate);
        enterDate.setHint("Date");
        enterDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        tableRow.addView(enterDate);
        this.enterDate = enterDate;

        EditText enterWeight = new EditText(context);
        setStyling(enterWeight);
        enterWeight.setHint("Weight in KG");
        enterWeight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tableRow.addView(enterWeight);
        this.enterWeight = enterWeight;

        tableLayout.addView(tableRow);

        this.saveButton = view.findViewById(R.id.button);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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