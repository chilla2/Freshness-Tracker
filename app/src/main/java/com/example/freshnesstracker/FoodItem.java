package com.example.freshnesstracker;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * FoodItem contains an Id, day, month, year, name, foodType, quantity,
 * and boolean value titled isExpired. The class includes getters and setters.
 */

@IgnoreExtraProperties
public class FoodItem {
    String itemId;
    int day;
    int month;
    int year;
    String name;
    String foodType;
    Boolean isExpired;
    int quantity;

    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FoodItem(String itemId, int day, int month, int year, String name, String foodType, int quantity) {
        this.itemId = itemId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.name = name;
        this.foodType = foodType;
        this.quantity = quantity;
    }

    public String getItemId(){return itemId; }
    public String getName(){return name;}
    public int getDay(){return day;}
    public int getMonth(){return month;}
    public int getYear(){return year;}
    public String getFoodType(){return foodType;}
    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setIsExpired(Boolean expired) {isExpired = expired;}
    public Boolean getIsExpired() {return isExpired; }

}



