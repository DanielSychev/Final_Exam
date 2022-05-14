package com.example.final_exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AnsFragment extends Fragment {
    Integer i=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ans_fragment, container, false);
        TextView verno=view.findViewById(R.id.verno);
        verno.setText(i.toString());
        i++;
        return view;
    }
}