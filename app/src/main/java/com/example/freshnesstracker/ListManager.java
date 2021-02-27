package com.example.freshnesstracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListManager {
    ArrayList<FoodItem> inventory;

    private void sortByExpiry(ArrayList<FoodItem> foodList){

        Comparator<FoodItem> dateComparator = new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem o1, FoodItem o2) {
                return o1.date.compareTo(o2.date);
            }
        };

        Collections.sort(inventory, dateComparator);


    }
    // Process: Sort Inventory by date. Create new ArrayList of FoodItems
    // Iterate through inventory and add the items of specified type.
    // Check to see if the array is empty before returning results.
    public ArrayList<FoodItem> searchByType(FoodType foodType){

        ArrayList<FoodItem>  listByType = new ArrayList<>();
        for(FoodItem i : inventory){
            if (i.getFoodType() == foodType){
                listByType.add(i);
            }
        }
        if (listByType.size() < 1){
            //return error message and stay on search activity.
            return null;
        }
        else {
            return listByType;
        }
    }
    // Process: Create new ArrayList to hold search results.
    // Check if name contains the search parameter (not case sensitive.)
    // If true add to new Arraylist.
    // Check to see if the array is empty before returning results.

    public ArrayList<FoodItem> searchByName(String searchName){

        ArrayList<FoodItem>  listByName = new ArrayList<>();
        searchName = searchName.toLowerCase();
        for(FoodItem i : inventory){
            String lowName = i.getName().toLowerCase();
            if (lowName.contains(searchName)){
                listByName.add(i);
            }
        }
        if (listByName.size() < 1){
            //return error message and stay on search activity.
            return null;
        }
        else{
            return listByName;
        }
    }


    public void insertFoodItem(FoodItem foodItem){
        // add FoodItem to inventory and sort (and save to file?)
    }


    public ArrayList<String> makeDisplayStringArray(){
        // make a list for displaying info in ListView (if that is what we decide is needed.)
        return null;
    }
}
