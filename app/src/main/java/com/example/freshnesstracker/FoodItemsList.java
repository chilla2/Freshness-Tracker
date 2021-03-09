package com.example.freshnesstracker;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FoodItemsList extends ArrayAdapter<FoodItem> {
    private Activity context;
    List<FoodItem> foodItems;

    public FoodItemsList(Activity context, List<FoodItem> foodItems) {
        super(context, R.layout.layout_items_list, foodItems);
        this.context = context;
        this.foodItems = foodItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_items_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        FoodItem foodItem  = foodItems.get(position);
        textViewName.setText(foodItem.getName());
        textViewGenre.setText(foodItem.getFoodType().toString());

        return listViewItem;
    }
}