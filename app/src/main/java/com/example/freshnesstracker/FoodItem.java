package com.example.freshnesstracker;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class FoodItem {
    public String id;
    public Date date;
    public String name;
    public String foodType;
    private Boolean isExpired;
    public int day;
    public int month;
    public int year;

    private static final String TAG = "FoodItemClass";

    //calendar class to get month/date/year
    private Calendar calendar = Calendar.getInstance();

    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FoodItem(String id, int year, int month, int day, String name, String foodType) {
        this.id = id;
        this.date = new Date(year, month, day);
        this.name = name;
        this.foodType = foodType;
        this.day = day;
        this.month = month;
        this.year = year;
        //calendar.setTime(date);

        //Log.d(TAG, "Date given: " + date);
        //Log.d(TAG, "Calendar date set: " + getFormattedDate());
    }


    public Date getDate(){return date;}
    public void setDate(Date aDate){ this.date = aDate;}


    public String getFoodType(){return foodType;}
    public void setFoodType(String aFoodType){this.foodType = aFoodType;}


    public String getName(){return name;}
    public void setName(String aName){this.name = aName;}

    public String getId(){return id;}
    public int getDay(){return day;}
    public int getMonth(){return month;}
    public int getYear(){return year;}

    // save food item to database...
    public void saveFoodItem(){

    }

    public String getFormattedDate() {

        //update the calendar
        calendar.setTime(this.date);

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        String formattedDate = month + "/" + day + "/" + year;
        return formattedDate;
    }

}


