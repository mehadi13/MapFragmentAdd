package com.example.mehadihossain.mapfragmentadd.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehadihossain.mapfragmentadd.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mehadi Hossain on 1/6/2017.
 */

public class ShapeAndDistanceFragment extends Fragment {

    RadioGroup radioGroup;
    EditText editText;
    Communicator communicator;
    Double dist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dist = getArguments().getDouble("dis");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.shape_and_distance_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText = (EditText) getActivity().findViewById(R.id.editText);
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
        Button shapeDistance = (Button) getActivity().findViewById(R.id.shapDistance);
        communicator = (Communicator) getActivity();

        editText.setText(Double.toString(dist));

        shapeDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                int shapeId = -1;
                Toast.makeText(getActivity(), Integer.toString(selectedId), Toast.LENGTH_SHORT).show();
                double dis = Double.parseDouble(editText.getText().toString());
                if (selectedId>-1 && dis > 0.0) {
                    dis = dis / 1000000;
                    switch (selectedId) {
                        case R.id.radioButtonTriangle:
                            shapeId = 1;
                            break;
                        case R.id.radioButtonRectangle:
                            shapeId = 2;
                            break;
                        case R.id.radioButtonHexagonal:
                            shapeId = 3;
                            break;
                    }
                    communicator.drawShape(shapeId,dis);
                }
            }
        });
    }
    public interface Communicator{
        void drawShape(int shapeId,double dis);
    }
}

