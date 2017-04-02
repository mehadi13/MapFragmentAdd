package com.example.mehadihossain.mapfragmentadd.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mehadihossain.mapfragmentadd.R;

import java.text.DecimalFormat;

/**
 * Created by Mehadi Hossain on 1/16/2017.
 */



public class InfoFragment extends Fragment {

    Double area;
    int trees;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trees = getArguments().getInt("trees");
        area = getArguments().getDouble("area");
        return inflater.inflate(R.layout.info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView treeTextView = (TextView) getActivity().findViewById(R.id.numOftree);
        TextView areaTextView = (TextView) getActivity().findViewById(R.id.area);

        treeTextView.setText( Integer.toString(trees));
       // areaTextView.setText( Double.toString(area)+"sq miter");
        areaTextView.setText(new DecimalFormat("##.###").format(area)+" sq meter");

    }
}



