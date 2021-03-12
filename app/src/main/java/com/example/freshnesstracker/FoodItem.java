package com.example.freshnesstracker;

import java.util.Calendar;
import java.util.Date;

public class FoodItem {

    public Date date;
    public String name;
    public FoodType foodType;
    private Boolean isExpired;

    //calendar class to get month/date/year
    private Calendar calendar = Calendar.getInstance();

    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FoodItem(Date date, String name, FoodType foodType) {
        this.date = date;
        this.name = name;
        this.foodType = foodType;
        calendar.setTime(date);
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

    public String getFormattedDate() {
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        String formattedDate = month + "/" + day + "/" + year;
        return formattedDate;
    }

}
