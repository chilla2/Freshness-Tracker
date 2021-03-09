package com.example.freshnesstracker;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FoodItem {
    public String itemId;
    public int day;
    public int month;
    public int year;
    public String name;
    public String foodType;
    private Boolean isExpired;

    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FoodItem(String itemId, int day, int month, int year, String name, String foodType) {
        this.itemId = itemId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.name = name;
        this.foodType = foodType;
    }

    public String getItemId() {
        return itemId;
    }

    public Integer getDay(){return day;}
    public Integer getMonth(){return month;}
    public Integer getYear(){return year;}

    public String getFoodType(){return foodType;}

    public String getName(){return name;}

}
