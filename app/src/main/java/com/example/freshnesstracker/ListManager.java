
package com.example.freshnesstracker;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/** The list manager takes the list that is generated from the database data and sorts it by whatever criteria is passed in
 *
 */

public class ListManager {
    ArrayList<FoodItem> foodItems;
    ArrayList<FoodItem> workingList;
    static private final String TAG = "ListManager";

    public ListManager(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
        workingList = new ArrayList<>(foodItems);
    }

    public void sortByExpiry(){
        if (workingList != null) {
            Comparator<FoodItem> dateComparator = (o1, o2) -> {
                Date date1 = new Date(o1.year, o1.month, o1.day);
                Date date2 = new Date(o2.year, o2.month, o2.day);
                return date1.compareTo(date2);
            };
            Collections.sort(workingList, dateComparator);
        }
    }


    // Process: Create new ArrayList to hold search results.
    // Check if name contains the search parameter (not case sensitive.)
    // If true add to new Arraylist.
    // Check to see if the array is empty before returning results.
    public ArrayList<FoodItem> searchByName(String searchName) {
        ArrayList<FoodItem> listByName = new ArrayList<>();
        searchName = searchName.toLowerCase();
        for (FoodItem i : foodItems) {
            String lowName = i.getName().toLowerCase();
            if (lowName.contains(searchName)) {
                listByName.add(i);
            }
        }
        if (listByName.size() < 1) {
            //return error message and stay on search activity.
            return null;
        } else {
            return listByName;
        }
    }
}