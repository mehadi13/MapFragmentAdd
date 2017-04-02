package com.example.mehadihossain.mapfragmentadd.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehadihossain.mapfragmentadd.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mehadi Hossain on 1/6/2017.
 */

public class TreeListFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int[] fruitsDis,vegeDis,woodDis;
    Communicator communicator;
    PopupWindow mpopup;
    View popUpView;
    Button okButton;
    int groupPosition,childPosition;
    TextView nameTextView,h1TextView,h2TextView,d1TextView,d2TextView,linkTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tree_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        communicator = (Communicator) getActivity();

        //prepear a popup window
        prepearPopup();
        nameTextView = (TextView) popUpView.findViewById(R.id.nameTextView);
        h1TextView = (TextView) popUpView.findViewById(R.id.h1TextView);
        d1TextView = (TextView) popUpView.findViewById(R.id.d1TextView);
        h2TextView = (TextView) popUpView.findViewById(R.id.h2TextView);
        d2TextView = (TextView) popUpView.findViewById(R.id.d2TextView);
        linkTextView = (TextView) popUpView.findViewById(R.id.webLinkTextView);
        okButton = (Button) popUpView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupPosition==1){
                    communicator.setDistance(fruitsDis[childPosition]/100.0);
                }else
                communicator.setDistance(fruitsDis[childPosition]);
                mpopup.dismiss();
            }
        });

        //get data from xml


        //load distance data
        fruitsDis = getResources().getIntArray(R.array.fruits);
        vegeDis = getResources().getIntArray(R.array.vegetables);
        woodDis = getResources().getIntArray(R.array.wood);

        // get the listview
        expListView = (ExpandableListView) getActivity().findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                loadDataInPopupWindow(groupPosition,childPosition);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);


                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();


               // Toast.makeText(getActivity(),Integer.toString(groupPosition)+" "+Integer.toString(childPosition)+" "+Integer.toString(fruitsDis[childPosition]),Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    private void loadDataInPopupWindow(int groupPosition, int childPosition) {
        this.childPosition = childPosition;
        this.groupPosition = groupPosition;
        String[] allInformation = new String[0];
        switch (groupPosition){
                    case 0:
                        allInformation = getActivity().getResources().getStringArray(R.array.fruitsTreeNames);

                       // communicator.setDistance(fruitsDis[childPosition]);
                        break;
                    case 1:
                        allInformation = getActivity().getResources().getStringArray(R.array.vegetablesTreeNames);
                       // communicator.setDistance(vegeDis[childPosition]);
                        break;
                    case 2:
                        allInformation = getActivity().getResources().getStringArray(R.array.woodTreeNames);
                       // communicator.setDistance(woodDis[childPosition]);
                        break;
                }
       // nameTextView.setText(allInformation[0]);
        try {
            JSONObject jsonObject = new JSONObject(allInformation[childPosition]);
            nameTextView.setText(jsonObject.getString("name"));
            setTestAfterFilter(jsonObject.getString("h1"),h1TextView);
            setTestAfterFilter(jsonObject.getString("d1"),d1TextView);
            setTestAfterFilter(jsonObject.getString("h2"),h2TextView);
            setTestAfterFilter(jsonObject.getString("d2"),d2TextView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setTestAfterFilter(String s, TextView textView) {
        s = s.replaceAll("@@",",");
        s = s.replaceAll("@"," ");
        textView.setText(s);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Fruits");
        listDataHeader.add("Vegetables");
        listDataHeader.add("Wood");

        // Adding child data
        List<String> fruits = new ArrayList<String>();
        fruits.add("Mango");
        fruits.add("Lychee");
        fruits.add("Banana");

        List<String> vegetables = new ArrayList<String>();
        vegetables.add("Capsicum");
        vegetables.add("Cauliflower");
        vegetables.add("Potato");

        List<String> wood = new ArrayList<String>();
        wood.add("Mahogany");
        wood.add("Segun");
        wood.add("Neem");

        listDataChild.put(listDataHeader.get(0), fruits); // Header, Child data
        listDataChild.put(listDataHeader.get(1), vegetables);
        listDataChild.put(listDataHeader.get(2), wood);
    }

    public interface Communicator{
        void setDistance(double distance);
    }

    void prepearPopup(){

        popUpView = getActivity().getLayoutInflater().inflate(R.layout.tree_information,
                null); // inflating popup layout
        mpopup = new PopupWindow(popUpView, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true); // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
         // Displaying popup
    }



}