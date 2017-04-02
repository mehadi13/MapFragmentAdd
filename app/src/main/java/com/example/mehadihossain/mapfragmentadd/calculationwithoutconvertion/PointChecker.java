package com.example.mehadihossain.mapfragmentadd.calculationwithoutconvertion;

/**
 * Created by Mehadi Hossain on 12/19/2016.
 */



public class PointChecker{

    int polyCorners;  //=  how many corners the polygon has (no repeats)
    double polyX[];      //=  horizontal coordinates of corners
    double polyY[];      //=  vertical coordinates of corners
    //=  point to be tested

    // The following global arrays should be allocated before calling these functions:
    double constant[]; //= storage for precalculated constants (same size as polyX)
    double multiple[]; // = storage for precalculated multipliers (same size as polyX)

    public PointChecker (int polyCorners, double[] polyX, double[] polyY) {
        this.polyCorners = polyCorners;
        this.polyX = polyX;
        this.polyY = polyY;
        constant = new double[polyCorners];
        multiple = new double[polyCorners];

        precalc_values();
    }

    void precalc_values() {

        int i, j = polyCorners - 1;

        for (i = 0; i < polyCorners; i++) {
            if (polyY[j] == polyY[i]) {
                constant[i] = polyX[i];
                multiple[i] = 0;
            } else {
                constant[i] = polyX[i] - (polyY[i] * polyX[j]) / (polyY[j] - polyY[i]) + (polyY[i] * polyX[i]) / (polyY[j] - polyY[i]);
                multiple[i] = (polyX[j] - polyX[i]) / (polyY[j] - polyY[i]);
            }
            j = i;
        }
    }

    public boolean pointInPolygon(double x, double y) {

        int i, j = polyCorners - 1;
        boolean oddNodes = false;

        for (i = 0; i < polyCorners; i++) {
            if ((polyY[i] < y && polyY[j] >= y
                    || polyY[j] < y && polyY[i] >= y)) {
                oddNodes ^= (y * multiple[i] + constant[i] < x);
            }
            j = i;
        }

        return oddNodes;
    }
}