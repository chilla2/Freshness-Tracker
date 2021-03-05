package com.example.freshnesstracker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class ListManager {
    ArrayList<FoodItem> inventory;

    public ListManager(ArrayList<FoodItem> inventory) {
        this.inventory = inventory;
    }

    private void saveItem(FoodType foodItem) {
        //April: I added this to the AddItemActivity
    }



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
