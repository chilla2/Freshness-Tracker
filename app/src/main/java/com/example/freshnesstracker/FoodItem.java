package com.example.freshnesstracker;

import java.util.Date;

public class FoodItem {

    public Date date;
    public String name;
    public String category;
    private Boolean isExpired;

    public FoodItem(Date date, String name, String category) {
        this.date = date;
        this.name = name;
        this.category = category;
    }



    // getters and setters
    /*

    public Date getDate(){return date;}
    public void setDate(Date aDate){ this.date = aDate;}


    public FoodType getFoodType(){return foodType;}
    public void setFoodType(FoodType aFoodType){this.foodType = aFoodType;}


    public String getName(){return name;}
    public void setName(String aName){this.name = aName;}

    // save food item to external file...
    public void saveFoodItem(){

    }
    */


}
