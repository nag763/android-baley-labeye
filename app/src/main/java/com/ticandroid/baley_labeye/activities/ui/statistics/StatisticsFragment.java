package com.ticandroid.baley_labeye.activities.ui.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.ui.evaluer.EvaluerFragment;

public class StatisticsFragment extends Fragment {


    private transient TextView text;
    public static Fragment newInstance() {
        return (new StatisticsFragment());
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        text = root.findViewById(R.id.text);
        return root;
    }
}