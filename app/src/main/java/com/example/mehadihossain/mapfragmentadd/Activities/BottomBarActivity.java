package com.example.mehadihossain.mapfragmentadd.Activities;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mehadihossain.mapfragmentadd.Fragments.InfoFragment;
import com.example.mehadihossain.mapfragmentadd.Fragments.ShapeAndDistanceFragment;
import com.example.mehadihossain.mapfragmentadd.Fragments.TreeListFragment;
import com.example.mehadihossain.mapfragmentadd.R;
import com.example.mehadihossain.mapfragmentadd.calculationwithoutconvertion.OriginCalculation;
import com.example.mehadihossain.mapfragmentadd.calculationwithoutconvertion.PointChecker;
import com.example.mehadihossain.mapfragmentadd.calculationwithoutconvertion.PointGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public class BottomBarActivity extends AppCompatActivity implements OnMapReadyCallback,ShapeAndDistanceFragment.Communicator,TreeListFragment.Communicator {

    private GoogleMap mMap;
    private ArrayList<LatLng> latLngs;
    private ArrayList<LatLng> borderPointLatLngs = new ArrayList<LatLng>();
    private OriginCalculation originCalculation;
    private ArrayList<LatLng> points;
    private PointGenerator pointGenerator;
    private ArrayList<LatLng> generatedPoints;
    private PointChecker pointChecker;
    private BottomBar bottomBar;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton printButton;
   // TextView numTextView;

    double distance = 0;
    double radius = 0.1;
    double area = 0.0;
    int noOftrees = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        printButton = (FloatingActionButton) findViewById(R.id.printActionButton);



        doCalculations();

        //numTextView = (TextView) findViewById(R.id.numtextView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BottomBarActivity.this, LandShapeActivity.class));
            }
        });

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_calls) {
                    printInvisible();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container1, new TreeListFragment(), "myfrag").commit();
                } else if (tabId == R.id.tab_groups) {

                    initialiseMap();
                    printButton.setVisibility(View.VISIBLE);
                    //fragmentTransaction.replace(R.id.contentContainer,new MapsActivity());
                }else if(tabId == R.id.info_tab){
                    printInvisible();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("area",area);
                    bundle.putInt("trees",noOftrees);
                    InfoFragment infoFragment = new InfoFragment();
                    infoFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container1,infoFragment,"infoFrag").commit();
                }
                else if (tabId == R.id.tab_chats) {
                    printInvisible();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("dis",distance);
                    ShapeAndDistanceFragment shapeAndDistanceFragment = new ShapeAndDistanceFragment();
                    shapeAndDistanceFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container1, shapeAndDistanceFragment , "shap").commit();
                }

            }
        });

    }

    private void initialiseMap() {
        android.support.v4.app.FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        SupportMapFragment mFRaFragment = new SupportMapFragment();

        mTransaction.replace(R.id.container1, mFRaFragment, "map");
        mTransaction.commit();
        mFRaFragment.getMapAsync(this);

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = latLngs.get((int)(latLngs.size()/2));
        mMap.addMarker(new MarkerOptions().position(borderPointLatLngs.get(0)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        PolygonOptions polygonOptions = new PolygonOptions();
        for (int i=0;i<borderPointLatLngs.size();i++){
            polygonOptions.add(borderPointLatLngs.get(i));
        }
        polygonOptions.add(borderPointLatLngs.get(0));
        googleMap.addPolygon(polygonOptions);

        //Toast.makeText(this,Integer.toString(latLngs.size())+" "+Integer.toString(borderPointLatLngs.size()),Toast.LENGTH_LONG).show();
        Toast.makeText(this,"Number of Trees: "+Integer.toString(latLngs.size()),Toast.LENGTH_LONG).show();

       // numTextView.setText("Number of Trees: "+Integer.toString(latLngs.size()));

        CircleOptions circleOptions = new CircleOptions()
                .center(sydney)
                .radius(.05*distance)
                .fillColor(Color.TRANSPARENT)  //default
                .strokeColor(0x10000000)
                .strokeWidth(5); // In meters

        CircleOptions center = new CircleOptions()
                .center(sydney)
                .radius(.1)
                .strokeColor(Color.GREEN); // In meters

        for (int i=0;i<latLngs.size();i++){
            LatLng latLng = latLngs.get(i);

            circleOptions.center(latLng);
            mMap.addCircle(circleOptions);

            center.center(latLng);
            mMap.addCircle(center);
            //  mMap.addMarker(new MarkerOptions().position(latLng));
        }

        noOftrees = latLngs.size();
        //area calculation
        area = areaCal();
        Toast.makeText(this,"Area:"+ Double.toString(area),Toast.LENGTH_SHORT).show();
    }



    private double areaCal()
    {
        ArrayList<Double> lats = new ArrayList<Double>();
        ArrayList<Double> lons = new ArrayList<Double>();


        double sum=0;
        double prevcolat=0;
        double prevaz=0;
        double colat0=0;
        double az0=0;

        for(int i= 0;i<borderPointLatLngs.size();i++){
            lats.add(borderPointLatLngs.get(i).latitude);
            lons.add(borderPointLatLngs.get(i).longitude);
        }

        for (int i=0;i<lats.size();i++)
        {
            double colat=2*Math.atan2(Math.sqrt(Math.pow(Math.sin(lats.get(i)*Math.PI/180/2), 2)+ Math.cos(lats.get(i)*Math.PI/180)*Math.pow(Math.sin(lons.get(i)*Math.PI/180/2), 2)),Math.sqrt(1-  Math.pow(Math.sin(lats.get(i)*Math.PI/180/2), 2)- Math.cos(lats.get(i)*Math.PI/180)*Math.pow(Math.sin(lons.get(i)*Math.PI/180/2), 2)));
            double az=0;
            if (lats.get(i)>=90)
            {
                az=0;
            }
            else if (lats.get(i)<=-90)
            {
                az=Math.PI;
            }
            else
            {
                az=Math.atan2(Math.cos(lats.get(i)*Math.PI/180) * Math.sin(lons.get(i)*Math.PI/180),Math.sin(lats.get(i)*Math.PI/180))% (2*Math.PI);
            }
            if(i==0)
            {
                colat0=colat;
                az0=az;
            }
            if(i>0 && i<lats.size())
            {
                sum=sum+(1-Math.cos(prevcolat  + (colat-prevcolat)/2))*Math.PI*((Math.abs(az-prevaz)/Math.PI)-2*Math.ceil(((Math.abs(az-prevaz)/Math.PI)-1)/2))* Math.signum(az-prevaz);
            }
            prevcolat=colat;
            prevaz=az;
        }
        sum=sum+(1-Math.cos(prevcolat  + (colat0-prevcolat)/2))*(az0-prevaz);
        return 5.10072E14* Math.min(Math.abs(sum)/4/Math.PI,1-Math.abs(sum)/4/Math.PI);
    }

    public void doCalculations(){
        //get Latlans point
        latLngs = (ArrayList<LatLng>) getIntent().getSerializableExtra("latlnglist");

        //store border point
        for (int i=0;i<latLngs.size();i++){
            borderPointLatLngs.add(latLngs.get(i));
        }

        //calculate origin and get all points
        originCalculation = new OriginCalculation(latLngs);
        points = originCalculation.getPoints();
        pointGenerator = new PointGenerator(points);

        //buidl polygon
        double x[] = new double[points.size()];
        double y[] = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x[i] = points.get(i).latitude;
            y[i] = points.get(i).longitude;
        }

        //check all points
        pointChecker = new PointChecker(points.size(), x, y);
    }

    @Override
    public void drawShape(int shapeId, double dis) {
            switch (shapeId) {
                case 1:
                    generatedPoints = pointGenerator.getTryangularGeneratedPoints(dis);
                    break;
                case 2:
                    generatedPoints = pointGenerator.getStrightGeneratedPoints(dis);
                    break;
                case 3:
                    generatedPoints = pointGenerator.getHexagonalGeneratedPoints(dis);
                    break;
            }
            points.clear();
            for (int i = 0; i < generatedPoints.size(); i++) {
                LatLng p = generatedPoints.get(i);
                if (pointChecker.pointInPolygon(p.latitude, p.longitude)) {
                    points.add(p);
                }
            }
            // textView.setText(s);
            //bordertextView.setText(Integer.toString(latLngs.size()));
            latLngs.clear();
            latLngs = originCalculation.getOriginalLatLngs(points);
        radius = dis;

        bottomBar.setDefaultTab(R.id.tab_groups);
        initialiseMap();

    }

    @Override
    public void setDistance(double distance) {
        bottomBar.setDefaultTab(R.id.tab_chats);
        if(R.id.tab_calls==bottomBar.getCurrentTabId())
            Toast.makeText(this,"true",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();
        this.distance = distance;
        Bundle bundle = new Bundle();
        bundle.putDouble("dis",distance);
        ShapeAndDistanceFragment shapeAndDistanceFragment = new ShapeAndDistanceFragment();
        shapeAndDistanceFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container1, shapeAndDistanceFragment , "shap").commit();
    }

    //print

    private void printInvisible(){
        if(printButton.getVisibility()==View.VISIBLE)
        printButton.setVisibility(View.INVISIBLE);
    }
    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.dr);*/
        photoPrinter.printBitmap("drpg", bitmap);
    }

    private void takeScreenshot() {
        View v1 = findViewById(R.id.container1).getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bm = v1.getDrawingCache();
       /* BitmapDrawable bitmapDrawable = new BitmapDrawable(bm);
        image = (ImageView) findViewById(R.id.imageView);
        image.setBackgroundDrawable(bitmapDrawable);*/
        doPhotoPrint(bm);
    }

    public void captureMapScreen()
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                doPhotoPrint(bitmap);
            }
        };

        mMap.snapshot(callback);

        // myMap is object of GoogleMap +> GoogleMap myMap;
        // which is initialized in onCreate() =>
        // myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_pass_home_call)).getMap();
    }

    public void doPrint(View view) {
        captureMapScreen();
      //  takeScreenshot();
    }
}
