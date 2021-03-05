package com.example.freshnesstracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BuildFakeInventory {


    public static ArrayList<FoodItem> buildFakeInventory() {
        // make a list of food types
        ArrayList<FoodType> typeList = new ArrayList<>();
        typeList.add(FoodType.Bakery);
        typeList.add(FoodType.Dairy);
        typeList.add(FoodType.Meat);
        typeList.add(FoodType.Dairy);
        typeList.add(FoodType.Dairy);

        //make a list of names
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Muffin");
        nameList.add("Milk");
        nameList.add("Pork Roast");
        nameList.add("Sour Cream");
        nameList.add("Cheese");

        //make a list of Dates * i'm using a funky "no longer supported" way
        // for now. It works for this but we will prob want to do something
        // different for the actual code.
        ArrayList<Date> dateDates = new ArrayList<Date>();
        dateDates.add(new Date(121, 2, 18));
        dateDates.add(new Date(121, 3, 18));
        dateDates.add(new Date(121, 11, 18));
        dateDates.add(new Date(121, 2, 15));
        dateDates.add(new Date(123, 2, 18));

        // Create the FoodItems and add to the fakeInventory.
        ArrayList<FoodItem> fakeInventory = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FoodItem p = new FoodItem(dateDates.get(i), nameList.get(i), typeList.get(i));
            fakeInventory.add(p);
        }
        return fakeInventory;
    }
}
