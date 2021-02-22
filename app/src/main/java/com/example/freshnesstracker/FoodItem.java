package com.example.freshnesstracker;

import java.util.Date;

public class FoodItem {
    Date date;
    FoodType foodType;
    String name;

    public FoodItem(Date aDate, FoodType aFoodType, String aName){
        this.date = aDate;
        this.foodType = aFoodType;
        this.name = aName;
    }
    // getters and setters
    public Date getDate(){return date;}
    public void setDate(Date aDate){ this.date = aDate;}


    public FoodType getFoodType(){return foodType;}
    public void setFoodType(FoodType aFoodType){this.foodType = aFoodType;}


    public String getName(){return name;}
    public void setName(String aName){this.name = aName;}

    // save food item to external file...
    public void saveFoodItem(){

    }


}
