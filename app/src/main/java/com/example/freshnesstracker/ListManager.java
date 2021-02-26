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

    public ArrayList<FoodItem> searchByType(FoodType  foodType){
        // function will create a new list(new ListManager?) with only specified type and then sort it by date
       /* sortByExpiry();

        ArrayList<FoodItem>  listByType = new ArrayList<>();
        for(FoodItem i : inventory){
            if (i.getFoodType() == foodType){
                listByType.add(i);
            }
        }
        return listByType;*/

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
