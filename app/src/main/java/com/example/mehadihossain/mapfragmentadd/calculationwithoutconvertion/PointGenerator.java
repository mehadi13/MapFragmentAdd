package com.example.mehadihossain.mapfragmentadd.calculationwithoutconvertion;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Mehadi Hossain on 12/19/2016.
 */

public class PointGenerator {
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    ArrayList<LatLng> generatedPoints = new ArrayList<LatLng>();

    double maxX,maxY,minX,minY;

    public PointGenerator(ArrayList<LatLng> points) {
        this.points = points;
        setMaxMinX();
        setMaxMinY();
    }

    public ArrayList<LatLng> getStrightGeneratedPoints(double interval) {
        generatedPoints.clear();
        for (double i = minX; i <= maxX; i= i+interval) {
            for (double j = maxY; j >= minY; j = j-interval) {
                generatedPoints.add(new LatLng(i, j));
            }
        }
        return generatedPoints;
    }

    public ArrayList<LatLng> getTryangularGeneratedPoints(double interval) {
        generatedPoints.clear();
        boolean even = true;
        for (double i = minX; i <= maxX; i= i+interval) {
            if(even) {
                for (double j = maxY; j >= minY; j = j - interval) {
                    generatedPoints.add(new LatLng(i, j));
                }
                even = false;
            }
            else{
                for (double j = maxY-(interval/2); j >= minY; j = j - interval) {
                    generatedPoints.add(new LatLng(i, j));
                }
                even = true;
            }
        }
        return generatedPoints;
    }

    public ArrayList<LatLng> getHexagonalGeneratedPoints(double interval) {
        generatedPoints.clear();
        boolean mid = true;
        int p = 0;
        for (double i = minX; i <= maxX; i= i+interval) {
            if(mid) {
                for (double j = maxY-(interval/2); j >= minY; j = j - interval) {
                    generatedPoints.add(new LatLng(i, j));
                }
                mid = false;
            }
            else{
                for (double j = maxY; j >= minY; j = j - interval) {
                    generatedPoints.add(new LatLng(i, j));
                }
                p++;
                if(p==2) {
                    mid = true;
                    p = 0;
                }
            }

        }
        return generatedPoints;
    }



    void setMaxMinX(){
        maxX = points.get(0).latitude;
        minX = points.get(0).latitude;
        for (int i = 1;i < points.size();i++){
            double x = points.get(i).latitude;
            if(maxX < x)
                maxX = x;
            if(minX > x)
                minX = x;
        }
    }

    void setMaxMinY(){
        maxY = points.get(0).longitude;
        minY = points.get(0).longitude;
        for (int i = 1;i < points.size();i++){
            double y = points.get(i).longitude;
            if(maxY < y)
                maxY = y;
            if(minY > y)
                minY = y;
        }
    }


}
