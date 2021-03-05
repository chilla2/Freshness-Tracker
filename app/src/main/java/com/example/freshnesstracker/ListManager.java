package com.example.freshnesstracker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListManager {
    ArrayList<FoodItem> inventory;

    public ListManager(ArrayList<FoodItem> inventory) {
        this.inventory = inventory;
    }

    private void saveItem(FoodType foodItem) {
        //April: I added this to the AddItemActivity
    }



    private void sortByExpiry(ArrayList<FoodItem> foodList){
        // function will sort the ArrayList by date.
    }
    public ArrayList<FoodItem> searchByType(FoodType  foodType){
        // function will create a new list(new ListManager?) with only specified type and then sort it by date
        return null;
    }

    public ArrayList<FoodItem> searchByName(String  name){
        //function will create a new list(new ListManager?) with only specified name and then sort it by date
        return null;
    }
    public void insertFoodItem(FoodItem foodItem){
        // add FoodItem to inventory and sort
    }
    public ArrayList<String> makeDisplayStringArray(){
        // make a list for displaying info in Listview
        return null;
    }
}
