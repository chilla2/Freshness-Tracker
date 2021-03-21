
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
    static private final String TAG = "ListManager";

    public ListManager(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public void sortByExpiry(ArrayList<FoodItem> foodItems){
        if (foodItems != null) {
            Comparator<FoodItem> dateComparator = (o1, o2) -> {
                Date date1 = new Date(o1.year, o1.month, o1.day);
                Date date2 = new Date(o2.year, o2.month, o2.day);
                return date1.compareTo(date2);
            };
            Collections.sort(foodItems, dateComparator);
        }
    }
}