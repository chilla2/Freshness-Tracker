package com.example.freshnesstracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;
    List<String> viewFoodList;
    private static final String FILE_NAME = "food-item-list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //temp: create an inventory list to display
        ListManager fakeInventory = new ListManager(BuildFakeInventory.buildFakeInventory());

        //Adapter and place holder list
        viewFoodList = new ArrayList<>(); //question--activity main here?
        arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_main, viewFoodList);

        //connecting ArrayAdapter to ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        //display fake inventory
        callDisplay(fakeInventory);

    }

    private void onAddFoodItem(View view){
    }
    private void onSearch(View view){
    }



    private void createFoodItem(String name, Integer date, String category) {
        //Create file object
        File foodItemList = new File(this.getFilesDir(), FILE_NAME);

        //Create objects to open file in read and write mode
        FileReader fileReader = null;
        FileWriter fileWriter = null;

        //Create objects to read from and write to the file
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String response = null;

        //Check for the existence of the file. If it does not exist, create it and write an empty string
       /*
        if (!foodItemList.exists()) {
            try {
                foodItemList.createNewFile();
                fileWriter = new FileWriter(foodItemList.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("{}");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Create JSON  and add food item properties
        JSONObject foodItem = new JSONObject();
        try {
            foodItem.put("Name", name);
            foodItem.put("Expiration Date", date);
            foodItem.put("Category", category);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Convert JsonObject to String Format
        String foodItemString = foodItem.toString();
        // Define the File Path and its Name
        File file = new File(context.getFilesDir(),FILE_NAME);
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try {
            bufferedWriter.write(foodItemString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
    }

    //called to display ListManager's to display; use on creation and for updates
    public void callDisplay(ListManager listManager) {

        //clear array adapter and current food view
        arrayAdapter.clear();
        viewFoodList.clear();

        //get required values from list; put filters here later?
        viewFoodList = listManager.makeDisplayStringArray();

        //add things to array adapter
        for (int i = 0; i < viewFoodList.size(); i++) {
            String lineToAdd = viewFoodList.get(i);
            arrayAdapter.add(lineToAdd);
        }

    }
}