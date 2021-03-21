package com.example.freshnesstracker;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.Date;

@IgnoreExtraProperties
public class FoodItem {
    public String itemId;
    public int day;
    public int month;
    public int year;
    public String name;
    public String foodType;
    private Boolean isExpired;
    public Date date;
    //calendar class to get month/date/year
    private Calendar calendar = Calendar.getInstance();

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
        this.date = new Date(year, month, day);
    }

    public String getItemId() {
        return itemId;
    }
    public String getName(){return name;}
    public Integer getDay(){return day;}
    public Integer getMonth(){return month;}
    public Integer getYear(){return year;}
    public String getFoodType(){return foodType;}
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
