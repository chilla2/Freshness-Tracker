package com.example.freshnesstracker;

import java.util.Date;

public class FoodItem {

    public Date date;
    public String name;
    public FoodType foodType;
    private Boolean isExpired;

    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FoodItem(Date date, String name, FoodType foodType) {
        this.date = date;
        this.name = name;
        this.foodType = foodType;
    }



    public Date getDate(){return date;}
    public void setDate(Date aDate){ this.date = aDate;}


    public FoodType getFoodType(){return foodType;}
    public void setFoodType(FoodType aFoodType){this.foodType = aFoodType;}


    public String getName(){return name;}
    public void setName(String aName){this.name = aName;}

    // save food item to database...
    public void saveFoodItem(){

    }

}
