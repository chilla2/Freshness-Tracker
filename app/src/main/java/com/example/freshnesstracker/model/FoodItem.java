package com.example.freshnesstracker.model;

import java.util.Date;

public class FoodItem {
    Date date;
    String name;
    String category;
    Boolean isExpired;


    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getExpired() {
        return isExpired;
    }
    public void setExpired(Boolean expired) {
        isExpired = expired;
    }
}
