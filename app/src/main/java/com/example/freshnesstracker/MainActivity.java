package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        //
    }
    private void onSearch(View view){

    }

    private void createFoodItem(String name, Integer date, String category) {

        JSONObject foodItem = new JSONObject();
        try {
            foodItem.put("Name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            foodItem.put("Expiration Date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            foodItem.put("Category", category);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Convert JsonObject to String Format
        String foodItemString = foodItem.toString();
        // Define the File Path and its Name
        File file = new File(this.getFilesDir(),FILE_NAME);
        FileWriter fileWriter = null;
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
    }
}