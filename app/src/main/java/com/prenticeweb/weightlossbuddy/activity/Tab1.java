package com.prenticeweb.weightlossbuddy.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
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
        View rootView = inflater.inflate(R.layout.fragment_tab_1, container, false);

        //random data
        String[] presidents = {"Dwight D. Eisenhower", "John F. Kennedy",
                "Lyndon B. Johnson"};

        Context context = getActivity();

        TableLayout tableLayout = rootView.findViewById(R.id.tableLayout1);

        for (int i = 0; i < presidents.length; i++) {
            TableRow tableRow = new TableRow(context);
            TextView textView = new TextView(context);
            textView.setPadding(10, 10, 10, 10);
            textView.setGravity(Gravity.CENTER);
            textView.setText(presidents[i]);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(20);
            textView.setBackgroundResource(R.color.lightYellow);
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }
        return rootView;
    }

}