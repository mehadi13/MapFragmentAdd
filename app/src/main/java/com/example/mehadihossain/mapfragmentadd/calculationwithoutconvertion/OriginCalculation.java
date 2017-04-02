package com.example.mehadihossain.mapfragmentadd.calculationwithoutconvertion;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Mehadi Hossain on 12/24/2016.
 */

public class OriginCalculation {
    private double stdX,stdY;
    private ArrayList<LatLng> latLngs;
    private ArrayList<LatLng> points;

    public OriginCalculation(ArrayList<LatLng> latLngs){
        this.latLngs = latLngs;
        points = new ArrayList<LatLng>();
        stdX = latLngs.get(0).latitude;
        stdY = latLngs.get(0).longitude;
        setOrigintoCenter();
    }

    private void setOrigintoCenter() {
        for (int i = 0;i < latLngs.size();i++){
            double x = latLngs.get(i).latitude - stdX;
            double y = latLngs.get(i).longitude - stdY;
            points.add(new LatLng(x,y));
        }
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public ArrayList<LatLng> getOriginalLatLngs(ArrayList<LatLng> points) {
        latLngs.clear();
        for (int i= 0;i<points.size();i++){
            LatLng p = points.get(i);
            double x = p.latitude + stdX;
            double y = p.longitude + stdY;
            latLngs.add(new LatLng(x,y));
        }
        return latLngs;
    }
}
