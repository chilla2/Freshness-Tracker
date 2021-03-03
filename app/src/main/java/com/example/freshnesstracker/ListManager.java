package com.example.freshnesstracker;

import java.util.ArrayList;
import java.util.List;

public class ListManager {
    ArrayList<FoodItem> inventory;
    public ListManager(ArrayList<FoodItem> aInventory){this.inventory = aInventory;}

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
    public List<String> makeDisplayStringArray(){
        // make a list for displaying info in Listview
        List<String> viewList = new ArrayList<>();

        //given filters as an arguement? Return string array

        //add all items to list
        for (int i = 0; i < inventory.size(); i++) {
            String result = inventory.get(i).name + " " + inventory.get(i).date.toString();

            viewList.add(result);
        }

        return viewList;
    }
}
