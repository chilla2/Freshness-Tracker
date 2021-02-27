package com.example.freshnesstracker;

import java.util.Date;

public class FoodItem {

    public Date date;
    public FoodType foodType;
    public String name;
    private Boolean isExpired;



    public FoodItem(Date _date, FoodType _foodType, String _name){
        this.date = _date;
        this.foodType = _foodType;
        this.name = _name;
        this.isExpired = false;
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
