package com.example.freshnesstracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;
    private static final String FILE_NAME = "food-item-list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}