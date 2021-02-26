package com.example.freshnesstracker;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.google.gson.Gson;

public class foodItemList {
    //this class handles interactions with the item list file



    //FoodItem newItem = new FoodItem(1, "Milk", "Dairy");
    Gson gson = new Gson();

    private static final String FILE_NAME = "food-item-list";


    private void createFoodItem(String name, Integer date, String category) {
        //Create file object

        //Create objects to open file in read and write mode
        FileReader fileReader = null;
        FileWriter fileWriter = null;

        //Create objects to read from and write to the file
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String response = null;

        //Check for the existence of the file. If it does not exist, create it and write an empty string

        /*if (!foodItemList.exists()) {
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
    }

*/


    }

}
