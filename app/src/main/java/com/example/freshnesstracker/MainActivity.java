package com.example.freshnesstracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;
    private static final String FILE_NAME = "food-item-list";

    //add in
    List<String> viewFoodList;

    //log cat tags
    public static final String TAG_CALL_DISPLAY = "CallDisplayFunc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Temporary work to get ListView to work with Array Adapter
        ListManager fakeInventory = new ListManager(BuildFakeInventory.buildFakeInventory());

        //Adapter and place holder list
        viewFoodList = new ArrayList<>(); //question--activity main here?
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewFoodList);

        //connecting ArrayAdapter to ListView
        ListView listView = findViewById(R.id.viewFoodList);
        listView.setAdapter(arrayAdapter);

        //display fake inventory
        Log.d(TAG_CALL_DISPLAY, "About to call function in Create");
        callDisplay(fakeInventory);
    }

    private void onAddFoodItem(View view){
    }
    private void onSearch(View view){
    }

    //called to display ListManager's to display; use on creation and for updates
    public void callDisplay(ListManager listManager) {


        //clear array adapter and current food view
        arrayAdapter.clear();
        viewFoodList.clear();

        //get required values from list; put filters here later?
        viewFoodList = listManager.makeDisplayStringArray();

        System.out.println(viewFoodList);
        Log.d(TAG_CALL_DISPLAY, "After view Food List Set");

        //get food list size
        int sizeList = viewFoodList.size();
        Log.d(TAG_CALL_DISPLAY, "The size of food list: " + sizeList); //correct
        Log.d(TAG_CALL_DISPLAY, "The first item in food list: " + viewFoodList.get(0)); //correct


        //add things to array adapter
        for (int i = 0; i < viewFoodList.size(); i++) {
            String lineToAdd = viewFoodList.get(i);
            arrayAdapter.add(lineToAdd);
            Log.d(TAG_CALL_DISPLAY, "Current index in displaying Loop: " + i);
        }

    }

}