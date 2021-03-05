package com.example.freshnesstracker;

import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
/*
public class ExampleUnitTest {
    @Test
    public void fileExists() {
        File fileName = new File("../FOOD_ITEM_LIST.json");
        assertTrue(fileName.exists());
    }
    @Test
    public void isExpired() {
        Date currentDate = new Date();
        FoodItem foodItem = new FoodItem((new Date(121, 1, 20)), "Milk", "Dairy");
        Date date = foodItem.date;
        int comparisonResult = date.compareTo(currentDate);
        assertTrue(comparisonResult < 0);
    }
    @Test
    public void isSorted() {
        FoodItem foodItem0 = new FoodItem((new Date(121, 0, 20)), "Milk", "Dairy");
        FoodItem foodItem1 = new FoodItem((new Date(121, 1, 01)), "Cream Cheese", "Dairy");
        FoodItem foodItem2 = new FoodItem((new Date(121, 2, 20)), "Bacon", "Meat");
        ArrayList<FoodItem> foodList = new ArrayList<FoodItem>();
        foodList.add(foodItem0);
        foodList.add(foodItem1);
        foodList.add(foodItem2);
        Boolean isSorted = true;
        for (int i = 0; i < 2; i++) {
            int j = i + 1;
            if (foodList.get(i).date.compareTo(foodList.get(j).date) > 0) {
                isSorted = false;
                break;
            }
        }
        assertTrue(isSorted);
    }
}*/
